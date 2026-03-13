package com.jobplatform.auth_service.service;

import com.jobplatform.auth_service.entity.UserAuth;
import com.jobplatform.auth_service.event.UserRegisteredEvent;
import com.jobplatform.auth_service.kafka.UserEventProducer;
import com.jobplatform.auth_service.repository.UserAuthRepository;
import com.jobplatform.auth_service.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserAuthRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserEventProducer userEventProducer;

    public AuthService(UserAuthRepository repository, JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder, UserEventProducer userEventProducer) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userEventProducer = userEventProducer;
    }

    public UserAuth register(UserAuth user) {
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserAuth saved = repository.save(user);

        userEventProducer.sendUserRegisteredEvent(UserRegisteredEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .authUserId(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .role(saved.getRole())
                .timestamp(LocalDateTime.now())
                .build());

        return saved;
    }

    public String login(String email, String password) {
        UserAuth user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }
}
