package com.gl.springsandbox.api.security;

import com.gl.springsandbox.api.security.filter.DefaultAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfigure {

    private final OncePerRequestFilter customAuthenticationFilter;

    public SecurityConfigure(DefaultAuthenticationFilter customAuthenticationFilter) {
        this.customAuthenticationFilter = customAuthenticationFilter;
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
        ((DefaultAuthenticationFilter) this.customAuthenticationFilter).setMatcherList(authenticationPathMatcher());
        http.addFilterAt(customAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class);

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

        // 라우터 옵션 추가
        http
                .requestMatchers().antMatchers("/api/user/**") // 인가 처리 경로 매칭
                .and()
                .authorizeHttpRequests()
                .antMatchers("/api/user/**").authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                .and()
                .logout();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    AndRequestMatcher authenticationPathMatcher() {
        List<AntPathRequestMatcher> list = Stream.of("/api/user/**")
                .map(AntPathRequestMatcher::new)
                .toList();
        return new AndRequestMatcher(list.toArray(new RequestMatcher[]{}));
    }

}
