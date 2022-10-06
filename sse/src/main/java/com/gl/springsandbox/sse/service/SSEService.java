package com.gl.springsandbox.sse.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.springsandbox.sse.configuration.ArmeriaServerConfigure;
import com.linecorp.armeria.common.sse.ServerSentEvent;
import com.linecorp.armeria.common.util.TimeoutMode;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.gl.springsandbox.sse.dto.StemMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

import static reactor.core.scheduler.Schedulers.fromExecutor;

@Service
@Slf4j
public class SSEService {
    private static final Sinks.Many<ServerSentEvent> sseStream;
    private static final ServerSentEvent PING_SSE;

    private final ObjectMapper objectMapper;

    private final Runnable ssePreventRequestTimeout = () -> {
        log.debug("### Refresh Request Timeout");
        ServiceRequestContext.current().setRequestTimeout(TimeoutMode.SET_FROM_NOW, Duration.ofSeconds(60));
    };

    static {
        sseStream = Sinks.many().multicast().directAllOrNothing();
        PING_SSE = ServerSentEvent.builder().comment("ping").build();
    }



    public SSEService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public static int currentSubscriberCount() {
        return sseStream.currentSubscriberCount();
    }

//    private Function<Flux<Long>, Flux<ServerSentEvent>> filterAndFlatMap = originFlux -> {
//        log.info("### transform interval ping to sse");
//        return originFlux.take(1).map(aLong -> PING_SSE);
//        return originFlux.map(aLong -> {
//            return PING_SSE;
//        });
//    };

    private Flux<ServerSentEvent> createSSEClientStream() {
        final String clientId = UUID.randomUUID().toString();
        return Flux.interval(Duration.ofSeconds(10))
                .publishOn(fromExecutor(ArmeriaServerConfigure.blockingTaskExecutor()))
                .map(idx -> {
                    ssePreventRequestTimeout.run();
                    return PING_SSE;
                })
                .doFirst(() -> log.info("### {} connect..", clientId))
                .doOnCancel(() -> log.info("### {} disconnect..", clientId))
                .doOnError(throwable -> log.error("### client connection error", throwable));
    }

    public Flux<ServerSentEvent> subscribeToSSEStream() {
        return sseStream.asFlux()
                .publishOn(fromExecutor(ArmeriaServerConfigure.blockingTaskExecutor()))
                .mergeWith(createSSEClientStream())
                .filter(Objects::nonNull);
//                .subscribeOn(fromExecutor(blockingTaskExecutor()));
    }

    public void emitPushMessage(StemMessage pushMessage) throws JsonProcessingException {
        sseStream.tryEmitNext(ServerSentEvent.builder().event("push")
                .data(objectMapper.writeValueAsString(pushMessage)).build());
    }

    public void emitBroadcastMessage(StemMessage broadcastMessage) throws JsonProcessingException {
        sseStream.tryEmitNext(ServerSentEvent.builder().event("broadcast")
                .data(objectMapper.writeValueAsString(broadcastMessage)).build());
    }
}
