package com.jobplatform.auth_service.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.jobplatform.auth_service.dto.LoginRequest;
import com.jobplatform.auth_service.entity.UserAuth;
import com.jobplatform.auth_service.event.UserRegisteredEvent;
import com.jobplatform.auth_service.kafka.UserEventProducer;
import com.jobplatform.auth_service.repository.UserAuthRepository;
import com.jobplatform.auth_service.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserAuthRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserEventProducer userEventProducer;

    public AuthController(UserAuthRepository repository, JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder, UserEventProducer userEventProducer) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userEventProducer = userEventProducer;
    }

    @PostMapping("/register")
    public ResponseEntity<UserAuth> register(@Valid @RequestBody UserAuth user) {
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserAuth saved = repository.save(user);

        userEventProducer.sendUserRegisteredEvent(UserRegisteredEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .authUserId(saved.getId())
                .email(saved.getEmail())
                .role(saved.getRole())
                .timestamp(LocalDateTime.now())
                .build());

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        UserAuth user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
