package com.jobplatform.auth_service.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.*;

import com.jobplatform.auth_service.entity.UserAuth;
import com.jobplatform.auth_service.repository.UserAuthRepository;
import com.jobplatform.auth_service.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserAuthRepository repository;
    private final JwtUtil jwtUtil;

    public AuthController(UserAuthRepository repository, JwtUtil jwtUtil) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
    }

    @SuppressWarnings("null")
    @PostMapping("/register")
    public UserAuth register(@RequestBody UserAuth user) {
        return repository.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserAuth request) {
        Optional<UserAuth> user = repository.findByEmail(request.getEmail());

        if (user.isPresent() &&
            user.get().getPassword().equals(request.getPassword())) {

            return jwtUtil.generateToken(
                    user.get().getEmail(),
                    user.get().getRole()
            );
        }

        throw new RuntimeException("Invalid credentials");
    }
}
