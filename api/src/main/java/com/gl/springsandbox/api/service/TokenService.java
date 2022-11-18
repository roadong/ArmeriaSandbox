package com.gl.springsandbox.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.springsandbox.api.entity.AccessToken;
import com.gl.springsandbox.api.repository.TokenRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper;

    public TokenService(TokenRepository tokenRepository,
                        ObjectMapper objectMapper) {
        this.tokenRepository = tokenRepository;
        this.objectMapper = objectMapper;
    }


    public String createToken(UserDetails user) throws JsonProcessingException {
        final String userAsString = objectMapper.writeValueAsString(user);
        tokenRepository.save(AccessToken.builder()
                .userDetailsAsString(userAsString)
                .token("token").build());
        return userAsString;
    }

    public String getToken(String userName) {
        return "token";
    }
}
