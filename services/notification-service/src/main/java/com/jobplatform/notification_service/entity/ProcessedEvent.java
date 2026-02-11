package com.jobplatform.notification_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "processed_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedEvent {
    @Id
    private String eventId;
}
