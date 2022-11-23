package com.gl.springsandbox.api.security;

import com.gl.springsandbox.api.security.filter.DefaultAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.Duration;

@Configuration
@EnableWebSecurity
public class SecurityConfigure {

    private final AuthenticationProvider defaultAuthenticationProvider;

    private final OncePerRequestFilter authenticationProcessingFilter;

    public SecurityConfigure(AuthenticationProvider defaultAuthenticationProvider,
                             DefaultAuthenticationFilter authenticationProcessingFilter) {
        this.defaultAuthenticationProvider = defaultAuthenticationProvider;
        this.authenticationProcessingFilter = authenticationProcessingFilter;
    }


    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.addAllowedOriginPattern("*");
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
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        http.addFilterAt(authenticationProcessingFilter, AbstractPreAuthenticatedProcessingFilter.class);

//        // 공통 옵션 추가
        http.csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .headers().frameOptions().disable().and()
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .sessionManagement(configurer -> configurer
//                        // 동시 세션 1개만
//                        .maximumSessions(1)
//                        // 동시 로그인 허용하지 않음
//                        .maxSessionsPreventsLogin(true)
                        // 세션 안 씀
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//        // 라우터 옵션 추가
        http
                .authenticationProvider(defaultAuthenticationProvider)
                .requestMatchers().antMatchers("/api/user/**") // 인가 처리 경로 매칭
                .and()
                .authorizeRequests()
                .antMatchers("/api/user/**").authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
