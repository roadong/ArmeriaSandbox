package com.magiell.springsandbox.sse.controller;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.sse.ServerSentEvent;
import com.linecorp.armeria.common.util.TimeoutMode;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.PathPrefix;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.ProducesEventStream;
import com.linecorp.armeria.server.streaming.ServerSentEvents;
import com.magiell.springsandbox.sse.service.SSEService;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.linecorp.armeria.server.ServiceRequestContext.current;

@PathPrefix("/sse-event")
@Component
@Slf4j
public class SSEController {

    private final SSEService sseService;

    public SSEController(SSEService sseService) {
        this.sseService = sseService;
    }

    @Get("/subscribe")
    @ProducesEventStream
    public Publisher<ServerSentEvent> subscribe() {
        log.info("### current {} client connecting...", SSEService.currentSubscriberCount());
        return sseService.subscribeToSSEStream();
    }
}
