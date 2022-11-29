package com.gl.springsandbox.api.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class DefaultAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationConverter authenticationConverter;

    private final AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;

    private final SecurityContextRepository securityContextRepository = new NullSecurityContextRepository();

    private AndRequestMatcher matcherList;

    private AuthenticationSuccessHandler successHandler;

    private AuthenticationFailureHandler failureHandler;

    public DefaultAuthenticationFilter(AuthenticationConverter authenticationConverter,
                                       AuthenticationManager authenticationManager,
                                       AuthenticationSuccessHandler successHandler,
                                       AuthenticationFailureHandler failureHandler) {
        this.authenticationConverter = authenticationConverter;
        this.authenticationManagerResolver = context -> authenticationManager;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    public void setMatcherList(AndRequestMatcher requestMatchers) {
        Assert.notNull(requestMatchers, "### 리퀘스트 매치 객체가 Null 일 수 없습니다");
        this.matcherList = requestMatchers;
    }

    public void setSuccessHandler(AuthenticationSuccessHandler handler) {
        Assert.notNull(handler, "### 인증 성공 핸들러가 Null 일 수 없습니다");
        this.successHandler = handler;
    }

    public void setFailureHandler(AuthenticationFailureHandler handler) {
        Assert.notNull(handler, "### 인증 실패 핸들러가 Null 일 수 없습니다");
        this.failureHandler = handler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(this.matcherList.matches(request)) {
            try {
                Authentication resultAuthentication = attemptAuthentication(request, response);
                if(resultAuthentication == null) {
                    filterChain.doFilter(request, response);
                    return;
                }
                successfulAuthentication(request, response, filterChain, resultAuthentication);
            } catch (AuthenticationException e) {
                unsuccessfulAuthentication(request, response, e);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        Authentication preAuthentication = authenticationConverter.convert(request);
        if(preAuthentication == null) {
            return null;
        }
        Authentication checkResultAuthentication = authenticationManagerResolver.resolve(request).authenticate(preAuthentication);
        if(checkResultAuthentication == null) {
            throw new ServletException("### 인증 객체가 Null 입니다");
        }

        return checkResultAuthentication;
    }

    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        this.securityContextRepository.saveContext(context, request, response);
        this.successHandler.onAuthenticationSuccess(request, response, chain, authentication);
    }

    private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        this.failureHandler.onAuthenticationFailure(request, response, failed);
    }

}
