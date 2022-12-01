package com.gl.springsandbox.api.security;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.UnaryOperator;

@Slf4j
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {

    @Getter
    @Setter
    private String realm;

    private final UnaryOperator<String> realmContent = value -> "Cookie realm=\"" + value + "\"";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        final String headerName = "WWW-Authenticate";
        response.addHeader(headerName, realmContent.apply(this.realm));
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    @Override
    public void afterPropertiesSet() {
        Assert.hasText(this.realm, "realm 이 반드시 특정되어야 합니다");
    }
}
