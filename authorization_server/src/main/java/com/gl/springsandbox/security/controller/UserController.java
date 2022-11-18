package com.gl.springsandbox.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.*;


/**
 * 인증 서버의 계정 처리 API
 */
@RestController
@RequestMapping("/api/v1/account")
@Slf4j
public class UserController {

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> login(@RequestParam("username") String username,
                                   @RequestParam("password") String password) {
        log.info("username:%s, password:%s".formatted(username, password));
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/sign/result")
    public ResponseEntity<?> loginResult(Authentication authentication) {
        log.info("auth : {}", authentication.getDetails().toString());
        return ResponseEntity.ok(authentication.getDetails());
    }
}
