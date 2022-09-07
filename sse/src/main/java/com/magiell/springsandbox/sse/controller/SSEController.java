package com.magiell.springsandbox.sse.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.sse.ServerSentEvent;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.*;
import com.magiell.springsandbox.sse.dto.StemMessage;
import com.magiell.springsandbox.sse.service.SSEService;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@PathPrefix("/sse-event")
@RestController
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

    @Post("/push")
    @ConsumesJson
    @ProducesJson
    public HttpResponse push(ServiceRequestContext ctx, HttpRequest req,
                             @RequestObject StemMessage message) throws JsonProcessingException {
        // TODO: filter tag로 채널을 분리하고 condition match 되서 나가게 구현
        log.info("### sse push request {}", message);
        sseService.emitPushMessage(message);

        return HttpResponse.of(HttpStatus.OK);
    }

    @Post("/broadcast")
    @ConsumesJson
    @ProducesJson
    public HttpResponse broadcast(ServiceRequestContext ctx, HttpRequest req,
                                  @RequestObject StemMessage message) throws JsonProcessingException {
        log.info("### sse push request {}", message);
        sseService.emitBroadcastMessage(message);

        return HttpResponse.of(HttpStatus.OK);
    }
}
