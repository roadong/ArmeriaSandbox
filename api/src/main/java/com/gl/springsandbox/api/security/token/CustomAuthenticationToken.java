package com.gl.springsandbox.api.security.token;

import com.gl.springsandbox.api.dto.UserAuthInfo;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;

import javax.persistence.Converter;
import java.io.Serial;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = -3252454326432632463L;

    public CustomAuthenticationToken(UserDetails info) {
        super(info.getAuthorities());
        setDetails(info);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return ((UserAuthInfo) getDetails()).getPassword();
    }

    @Override
    public Object getPrincipal() {
        return getDetails();
    }
}
