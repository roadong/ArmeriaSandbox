package com.gl.springsandbox.api.security.filter.handler;

import com.gl.springsandbox.api.security.DefaultAuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AuthFailureHandler implements AuthenticationFailureHandler {

    private final AuthenticationEntryPoint authenticationEntryPoint;

    public AuthFailureHandler(@Value("${:_atc}") String realmValue) throws Exception {
        final DefaultAuthenticationEntryPoint defaultAuthenticationEntryPoint = new DefaultAuthenticationEntryPoint();
        defaultAuthenticationEntryPoint.setRealm(realmValue);
        defaultAuthenticationEntryPoint.afterPropertiesSet();
        this.authenticationEntryPoint = defaultAuthenticationEntryPoint;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        this.authenticationEntryPoint.commence(request, response, exception);
    }
}
