package com.jobplatform.notification_service.kafka;

import com.jobplatform.notification_service.entity.ProcessedEvent;
import com.jobplatform.notification_service.event.ApplicationCreatedEvent;
import com.jobplatform.notification_service.repository.ProcessedEventRepository;
import com.jobplatform.notification_service.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEventConsumer {

    private final ProcessedEventRepository processedEventRepository;
    private final EmailService emailService;

    public ApplicationEventConsumer(ProcessedEventRepository processedEventRepository, EmailService emailService) {
        this.processedEventRepository = processedEventRepository;
        this.emailService = emailService;
    }

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 2000))
    @KafkaListener(topics = "application.created", groupId = "notification-group")
    public void consume(ApplicationCreatedEvent event) {
        String eventId = event.getEventId();

        if (processedEventRepository.existsById(eventId)) {
            System.out.println("Duplicate event ignored: " + eventId);
            return;
        }

        processedEventRepository.save(new ProcessedEvent(eventId));

        if (event.getUserEmail() != null && !event.getUserEmail().isBlank()) {
            emailService.sendApplicationConfirmation(
                    event.getUserEmail(),
                    event.getApplicationId(),
                    event.getJobId()
            );
        } else {
            System.out.println("No email available for userId=" + event.getUserId()
                    + ", skipping email notification for application " + event.getApplicationId());
        }
    }
}
