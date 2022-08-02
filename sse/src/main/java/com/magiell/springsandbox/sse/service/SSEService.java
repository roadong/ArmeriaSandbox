package com.magiell.springsandbox.sse.service;

import com.linecorp.armeria.common.sse.ServerSentEvent;
import com.linecorp.armeria.common.util.TimeoutMode;
import com.linecorp.armeria.internal.common.CancellationScheduler;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.magiell.springsandbox.sse.configuration.ArmeriaServerConfigure;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.*;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

import static com.magiell.springsandbox.sse.configuration.ArmeriaServerConfigure.blockingTaskExecutor;
import static reactor.core.scheduler.Schedulers.fromExecutor;

@Service
@Slf4j
public class SSEService{
    private static Sinks.Many<ServerSentEvent> sseStream;
    private static ServerSentEvent PING_SSE;

    private Runnable ssePreventRequestTimeout = () -> {
        log.info("### Refresh Request Timeout");
        ServiceRequestContext.current().setRequestTimeout(TimeoutMode.SET_FROM_NOW, Duration.ofSeconds(60));
    };

    static {
        sseStream = Sinks.many().multicast().directAllOrNothing();
        PING_SSE = ServerSentEvent.builder().comment("ping").build();
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
        SSEClientSubscriber subscriber = new SSEClientSubscriber();
        return Flux.interval(Duration.ofSeconds(5))
                .publishOn(fromExecutor(blockingTaskExecutor()))
                .map(idx -> {
                    ssePreventRequestTimeout.run();
                    return PING_SSE;
                })
                .doOnCancel(subscriber::dispose);
//                .subscribeOn(fromExecutor(blockingTaskExecutor()));
    }

    public Flux<ServerSentEvent> subscribeToSSEStream() {
        return sseStream.asFlux()
                .publishOn(fromExecutor(blockingTaskExecutor()))
                .mergeWith(createSSEClientStream())
                .filter(Objects::nonNull);
//                .subscribeOn(fromExecutor(blockingTaskExecutor()));
    }
}
