package com.magiell.springsandbox.sse.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/health")
@Slf4j
public class HealthCheckController {
    @GetMapping("/healthcheck")
    public Mono<ResponseEntity> healthCheck() {
        // log.info("health-check");
        return Mono.defer(() -> Mono.just(ResponseEntity.ok().build()));
    }
}
