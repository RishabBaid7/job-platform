package com.jobplatform.user_service.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.*;

import com.jobplatform.user_service.entity.User;
import com.jobplatform.user_service.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
}
