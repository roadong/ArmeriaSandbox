package com.gl.springsandbox.sse.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class WebfluxConfigure {
    private final ObjectMapper objectMapper;

//    @Value("${spring.redis.host}")
//    private String redisHost;
//    @Value("${spring.redis.port}")
//    private Integer redisPort;
//    @Value("${spring.redis.password}")
//    private String redisAuth;

    public WebfluxConfigure(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

//    @Bean
//    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
//        RedisConfiguration redisConfiguration = LettuceConnectionFactory.createRedisConfiguration(RedisURI
//                .builder()
//                .withHost(redisHost)
//                .withPort(redisPort)
//                .withPassword((CharSequence) redisAuth)
//                .withDatabase(10)
//                .build());
//
//        return new LettuceConnectionFactory(redisConfiguration);
//    }
}
