package com.jobplatform.user_service.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jobplatform.user_service.entity.User;
import com.jobplatform.user_service.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SuppressWarnings("null")
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @SuppressWarnings("null")
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }
}
