package com.gl.springsandbox.scg.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    public static final String MODIFY_TOKEN_KEY = "access_token";

    private final ModifyResponseBodyGatewayFilterFactory modifyFilterFactory;

    public AuthenticationFilter(ModifyResponseBodyGatewayFilterFactory factory) {
        super(Config.class);
        this.modifyFilterFactory = factory;
    }


    private static Publisher<JsonNode> rewriteBody(ServerWebExchange serverWebExchange, JsonNode bodyAsString) {
        // origin response body log
        log.info(bodyAsString.toPrettyString());
        // token exchange jwt to opaque
        bodyAsString = ((ObjectNode) bodyAsString).put(MODIFY_TOKEN_KEY, "temp");

        return Mono.just(bodyAsString);
    }

    @Override
    public GatewayFilter apply(Config config) {
        final ModifyResponseBodyGatewayFilterFactory.Config rewriteConfig = new ModifyResponseBodyGatewayFilterFactory.Config();
        rewriteConfig.setRewriteFunction(JsonNode.class, JsonNode.class, AuthenticationFilter::rewriteBody);
        return modifyFilterFactory.apply(rewriteConfig);
    }

    public static class Config {
        private String modifyTokenKey;

        public Config setModifyTokenKey(String modifyTokenKey) {
            Assert.hasText(modifyTokenKey, "modifyTokenKey must have a value");
            this.modifyTokenKey = modifyTokenKey;
            return this;
        }

        public String getModifyTokenKey() { return modifyTokenKey; }
    }
}
