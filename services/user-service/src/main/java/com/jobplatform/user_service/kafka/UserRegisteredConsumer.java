package com.jobplatform.user_service.kafka;

import com.jobplatform.user_service.entity.User;
import com.jobplatform.user_service.event.UserRegisteredEvent;
import com.jobplatform.user_service.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredConsumer {

    private final UserRepository userRepository;

    public UserRegisteredConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "user.registered", groupId = "user-service-group")
    public void consume(UserRegisteredEvent event) {
        if (userRepository.existsByEmail(event.getEmail())) {
            return;
        }
        String name = (event.getName() != null && !event.getName().isBlank())
                ? event.getName()
                : event.getEmail().split("@")[0];
        User user = User.builder()
                .name(name)
                .email(event.getEmail())
                .role(event.getRole())
                .build();
        userRepository.save(user);
    }
}
