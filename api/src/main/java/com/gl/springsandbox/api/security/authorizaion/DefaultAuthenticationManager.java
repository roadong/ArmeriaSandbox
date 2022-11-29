package com.gl.springsandbox.api.security.authorizaion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultAuthenticationManager implements AuthenticationManager {

    private final AuthenticationProvider provider;

    public DefaultAuthenticationManager(DefaultAuthenticationProvider provider) {
        this.provider = provider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return provider.authenticate(authentication);
    }
}
