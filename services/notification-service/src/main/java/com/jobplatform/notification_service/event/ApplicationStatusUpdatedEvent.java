package com.jobplatform.notification_service.event;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationStatusUpdatedEvent {
    private String eventId;
    private Long applicationId;
    private Long userId;
    private String userEmail;
    private String newStatus;
    private LocalDateTime timestamp;
}
