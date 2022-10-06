package com.gl.springsandbox.scg.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class CircuitBreakerController {
    @RequestMapping("/fallback")
    public Mono<String> trafficOverload() {
        return Mono.just("service temporarily unavailable. <br> try to request later.");
    }

    @RequestMapping("/service_failure")
    public Mono<String> failedService() {
        return Mono.just("failed to service");
    }
}
