package com.gl.springsandbox.scg.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {

    private final ModifyRequestBodyGatewayFilterFactory modifyRequestFilterFactory;

    public static class Config {

    }

    public AuthorizationFilter(ModifyRequestBodyGatewayFilterFactory factory) {
        super(Config.class);
        this.modifyRequestFilterFactory = factory;
    }

    private static Publisher<JsonNode> rewriteBody(ServerWebExchange serverWebExchange, JsonNode bodyAsString) {
        // origin response body log
        log.info(bodyAsString.toPrettyString());
        // token exchange jwt to opaque
        bodyAsString = ((ObjectNode) bodyAsString).put("access_key", "temp");

        return Mono.just(bodyAsString);
    }


    @Override
    public GatewayFilter apply(Config config) {
        final ModifyRequestBodyGatewayFilterFactory.Config modifyFilterFactoryConfig = new ModifyRequestBodyGatewayFilterFactory.Config();
        modifyFilterFactoryConfig.setRewriteFunction(JsonNode.class, JsonNode.class, AuthorizationFilter::rewriteBody);
        return modifyRequestFilterFactory.apply(modifyFilterFactoryConfig);
    }

}
