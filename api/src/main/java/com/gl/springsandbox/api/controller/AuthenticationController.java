package com.gl.springsandbox.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gl.springsandbox.api.dto.request.SignUpInfo;
import com.gl.springsandbox.api.security.token.AuthenticationTokenHelper;
import com.gl.springsandbox.api.service.CustomUserDetailService;
import com.gl.springsandbox.api.service.TokenService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final CustomUserDetailService userDetailService;

    private final TokenService tokenService;

    public AuthenticationController(CustomUserDetailService userDetailService,
                                    TokenService tokenService) {
        this.userDetailService = userDetailService;
        this.tokenService = tokenService;
    }

    @PostMapping("/account/signin")
    public ResponseEntity<?> authentication(@RequestBody String encryptAuth,
                                            HttpServletResponse response) throws JsonProcessingException {
        final String userName = "test";
        final String password = "1234";

        UserDetails userDetails = userDetailService.loadUserByUsername(userName);
        // check to password
        userDetailService.validPassword(userDetails, password);

        String token = tokenService.createToken(userDetails);

        // set to auth-token
        AuthenticationTokenHelper.setAuthTokeCookie(response, token);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/account/signup", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> signup(@RequestBody SignUpInfo accountInfo) {
        return ResponseEntity.noContent().build();
    }
}
