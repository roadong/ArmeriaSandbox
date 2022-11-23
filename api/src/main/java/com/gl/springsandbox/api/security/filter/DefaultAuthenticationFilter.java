package com.gl.springsandbox.api.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authorization.method.PreAuthorizeAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.PathMatcher;
import java.util.Arrays;

@Slf4j
@Component
public class DefaultAuthenticationFilter extends OncePerRequestFilter {

    private final String TOKEN_KEY = "_atc";

//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//            throws AuthenticationException {
//        final String token = getToken(request);
//
//        // preAuthentication (쿠키 토큰을 사전인증 토큰으로 구겨넣고 인증 절차를 밟는다)
//        return super.getAuthenticationManager().authenticate(new PreAuthenticatedAuthenticationToken(token, "N/A"));
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = getToken(request);
        SecurityContextHolder.getContext().setAuthentication(new PreAuthenticatedAuthenticationToken(token, "N/A"));
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        Cookie authCookie = getAuthCookie(request);
        return authCookie.getValue();
    }

    private Cookie getAuthCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(TOKEN_KEY))
                .findFirst()
                .orElseThrow(() -> new AuthenticationServiceException("#### 인증 쿠키가 존재하지 않습니다"));
    }



}
