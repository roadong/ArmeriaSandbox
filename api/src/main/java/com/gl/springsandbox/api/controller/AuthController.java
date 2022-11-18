package com.gl.springsandbox.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/basic")
    public ResponseEntity<?> basic(Authentication authentication) {
        return ResponseEntity.ok(authentication.getDetails().toString());
    }
}
