package com.gl.springsandbox.scg.filter;

import brave.Tracing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SCGLogFilter extends AbstractGatewayFilterFactory<SCGLogFilter.Config> {

    public SCGLogFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            Tracing.currentTracer()
                    .currentSpan()
                    .tag("gateway.request-id", request.getId());

            //log.info("### TRACE_ID : {}, SPAN_ID: {}", currentTraceId(), currentSpanId());

            return chain.filter(exchange)
                    .then(logAfterFilter(response));
        };
    }

    private static Mono<Void> logAfterFilter(ServerHttpResponse response) {
        return Mono.fromRunnable(() -> {
            log.info("Global Filter End : response code -> {}", response.getStatusCode());
            log.info("Custom Post");
        });
    }

    public static class Config {
        String test = "test";
    }
}