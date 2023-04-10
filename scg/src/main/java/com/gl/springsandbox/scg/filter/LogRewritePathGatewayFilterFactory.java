package com.gl.springsandbox.scg.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;

@Component
@Slf4j
public class LogRewritePathGatewayFilterFactory extends AbstractGatewayFilterFactory<LogRewritePathGatewayFilterFactory.Config> {

    public LogRewritePathGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                log.info("start rewrite Path : {}", exchange.getRequest().getPath());
                return new RewritePathGatewayFilterFactory()
                        .apply(config)
                        .filter(exchange, chain);
            }

            @Override
            public String toString() {
                return filterToStringCreator(LogRewritePathGatewayFilterFactory.this).toString();
            }
        };
    }

    private static Mono<Void> logAfterFilter(ServerWebExchange exchange) {
        return Mono.fromRunnable(() -> {
            log.info("end rewrite Path : response code -> {}", exchange.getRequest().getPath());
        });
    }

    public static class Config extends RewritePathGatewayFilterFactory.Config {
        @Override
        public String getRegexp() {
            return super.getRegexp();
        }

        @Override
        public RewritePathGatewayFilterFactory.Config setRegexp(String regexp) {
            return super.setRegexp(regexp);
        }

        @Override
        public String getReplacement() {
            return super.getReplacement();
        }

        @Override
        public RewritePathGatewayFilterFactory.Config setReplacement(String replacement) {
            return super.setReplacement(replacement);
        }
    }
}
