package com.gl.springsandbox.api.security.filter;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Component
public class DefaultAuthenticationConverter implements AuthenticationConverter {

    private final String TOKEN_KEY = "_atc";

    @Override
    public Authentication convert(HttpServletRequest request) {
        final String token = getToken(request);
        return new PreAuthenticatedAuthenticationToken(token, "N/A");
    }

    private String getToken(HttpServletRequest request) {
        Cookie authCookie = getAuthCookie(request);
        return authCookie.getValue();
    }

    private Cookie getAuthCookie(HttpServletRequest request) {
        List<Cookie> cookieList = request.getCookies() == null ? Collections.emptyList() : List.of(request.getCookies());
        return cookieList.stream()
                .filter(cookie -> cookie.getName().equals(TOKEN_KEY))
                .findAny()
                .orElseThrow(() -> new AuthenticationServiceException("#### 인증 쿠키가 존재하지 않습니다"));
    }

}
