package com.gl.springsandbox.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gl.springsandbox.api.dto.request.SignUpInfo;
import com.gl.springsandbox.api.security.crypt.CryptHandler;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import static com.gl.springsandbox.api.security.crypt.CryptUtils.selectCrypt;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final CustomUserDetailService userDetailService;

    private final TokenService tokenService;

    private final CryptHandler<String, String> cryptor;

    public AuthenticationController(CustomUserDetailService userDetailService,
                                    TokenService tokenService) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        this.userDetailService = userDetailService;
        this.tokenService = tokenService;
        this.cryptor = selectCrypt("decrypt-RSA");
    }

    @PostMapping("/account/signin")
    public ResponseEntity<?> authentication(@RequestBody String encryptAuth,
                                            HttpServletResponse response) throws JsonProcessingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        String decrypt = cryptor.decrypt(encryptAuth);
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
