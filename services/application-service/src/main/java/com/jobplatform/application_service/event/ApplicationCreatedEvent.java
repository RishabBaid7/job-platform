package com.jobplatform.application_service.event;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationCreatedEvent {

    private String eventId;
    private Long applicationId;
    private Long userId;
    private Long jobId;
    private LocalDateTime timestamp;
}
