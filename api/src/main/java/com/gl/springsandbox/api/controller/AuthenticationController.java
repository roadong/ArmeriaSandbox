package com.gl.springsandbox.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gl.springsandbox.api.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.attribute.UserPrincipalLookupService;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(UserDetailsService userDetailsService,
                                    TokenService tokenService,
                                    PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/basic")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> authentication(@RequestBody String encryptAuth,
                                            HttpServletResponse response) throws JsonProcessingException {
        final String userName = "test";
        final String password = "1234";
        UserDetails user = userDetailsService.loadUserByUsername(userName);
        boolean idPasswordCorrect = passwordEncoder.matches(password, user.getPassword());
        if(idPasswordCorrect) {
            final String token = tokenService.createToken(user);
            Authentication authentication = new PreAuthenticatedAuthenticationToken(user, password);
            response.addCookie(new Cookie("token", token));
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
