package com.jobplatform.user_service.event;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisteredEvent {
    private String eventId;
    private Long authUserId;
    private String email;
    private String role;
    private LocalDateTime timestamp;
}
