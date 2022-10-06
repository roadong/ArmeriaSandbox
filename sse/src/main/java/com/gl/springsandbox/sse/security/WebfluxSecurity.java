package com.gl.springsandbox.sse.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;

import java.time.Duration;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebfluxSecurity {

    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.addAllowedOrigin("*");
        corsConfig.setMaxAge(Duration.ofHours(1));

        corsConfig.addAllowedMethod(HttpMethod.GET);
        corsConfig.addAllowedMethod(HttpMethod.POST);
        corsConfig.addAllowedMethod(HttpMethod.OPTIONS);

        corsConfig.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                // https://github.com/spring-projects/spring-framework/issues/28575
                .requestCache(ServerHttpSecurity.RequestCacheSpec::disable)
                .securityMatcher(pathMatchers("/aaaa/**"))
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .accessDeniedHandler(new HttpStatusServerAccessDeniedHandler(HttpStatus.UNAUTHORIZED)));

        //        setWebFilterLogging(httpSecurity);

        return httpSecurity.build();
    }

    private void setWebFilterLogging(ServerHttpSecurity serverHttpSecurity) {
        for(SecurityWebFiltersOrder order : SecurityWebFiltersOrder.values()) {
            serverHttpSecurity.addFilterBefore(logBeforeWebFilter(order), order);
            serverHttpSecurity.addFilterAfter(logAfterWebFilter(order), order);
        }
    }

    private static WebFilter logBeforeWebFilter(SecurityWebFiltersOrder filterOrder) {
        return (exchange, chain) -> {
            log.info("Location: BEFORE {}", filterOrder);
            return chain.filter(exchange);
        };
    }

    private static WebFilter logAfterWebFilter(SecurityWebFiltersOrder filterOrder) {
        return (exchange, chain) -> {
            log.info("Location: AFTER {}", filterOrder);
            return chain.filter(exchange);
        };
    }

}
