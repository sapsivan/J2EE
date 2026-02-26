package com.example.paymentapp.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password) {

        if ("admin".equals(username) && "password".equals(password)) {

            UserDetails user = org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password("")
                    .roles("ADMIN")
                    .build();

            return jwtService.generateToken(user);
        }

        throw new RuntimeException("Invalid credentials");
    }
}