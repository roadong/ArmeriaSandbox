package com.gl.springsandbox.api.controller;

import com.gl.springsandbox.api.dto.User;
import com.gl.springsandbox.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody User userInfo) {
        long resultId = userService.insertUser(userInfo);
        return ResponseEntity.created(URI.create("/api/user/%d".formatted(resultId))).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable long id) {
        User userInfo = userService.getUser(id);
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifyUserInfo(@PathVariable long id,
                                            @RequestBody User userInfo) {
        User updateUser = userService.modifyUser(userInfo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        boolean isDeleted = userService.deleteUser(id);
        if(isDeleted) {
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }
}
