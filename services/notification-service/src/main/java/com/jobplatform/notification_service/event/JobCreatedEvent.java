package com.jobplatform.notification_service.event;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobCreatedEvent {
    private String eventId;
    private Long jobId;
    private String title;
    private String companyName;
    private String location;
    private Double salary;
    private String postedByEmail;
    private LocalDateTime timestamp;
}
