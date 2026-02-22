package com.example.paymentapp.security;

import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password) {

        if ("admin".equals(username) && "password".equals(password)) {
            return "LOGIN_SUCCESS";
        }

        throw new RuntimeException("Invalid credentials");
    }
}