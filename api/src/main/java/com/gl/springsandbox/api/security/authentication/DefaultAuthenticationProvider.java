package com.gl.springsandbox.api.security.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;


/**
 * AuthenticationProvider
 * 인증 전 객체를 검증하고 검증이 완료된 인증 객체를 반환 한다 (AuthenticationManager 에서 위임 호출 구현체)
 * 즉 여기서 인증 객체를 검증하고 결과를 리턴해야 함
 */
@Component
@Slf4j
public class DefaultAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(!supports(authentication.getClass())) return null;
        else return new TestingAuthenticationToken("", "");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
