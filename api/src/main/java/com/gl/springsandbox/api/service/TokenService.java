package com.gl.springsandbox.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.springsandbox.api.dto.UserAuthInfo;
import com.gl.springsandbox.api.entity.AccessToken;
import com.gl.springsandbox.api.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Qualifier;
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
                .userName(user.getUsername())
                .ttlSeconds(-1)
                .token("token").build());
        return "token";
    }

    public UserDetails getUserDetails(String token) throws JsonProcessingException {
        AccessToken accessToken = tokenRepository.findAccessTokenByToken(token).orElseThrow();
        return objectMapper.readValue(accessToken.getUserDetails(), UserAuthInfo.class);
    }


}
