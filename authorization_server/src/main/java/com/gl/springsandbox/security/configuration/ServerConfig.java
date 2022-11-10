package com.gl.springsandbox.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class ServerConfig {

    @Bean
    public CorsConfigurationSource configCors() {
        // all open
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.applyPermitDefaultValues();
        configuration.setAllowCredentials(Boolean.TRUE);

        configuration.addAllowedMethod(HttpMethod.OPTIONS);
        configuration.addAllowedMethod(HttpMethod.PUT);
        configuration.addAllowedMethod(HttpMethod.DELETE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


}
