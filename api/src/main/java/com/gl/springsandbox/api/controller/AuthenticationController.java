package com.gl.springsandbox.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @PostMapping("/basic")
    public ResponseEntity<?> authentication(@RequestBody String encryptAuth) {

        return ResponseEntity.ok().build();
    }
}
