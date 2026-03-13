package com.jobplatform.notification_service.kafka;

import com.jobplatform.notification_service.entity.ProcessedEvent;
import com.jobplatform.notification_service.event.ApplicationStatusUpdatedEvent;
import com.jobplatform.notification_service.repository.ProcessedEventRepository;
import com.jobplatform.notification_service.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStatusConsumer {

    private static final Logger log = LoggerFactory.getLogger(ApplicationStatusConsumer.class);

    private final ProcessedEventRepository processedEventRepository;
    private final EmailService emailService;

    public ApplicationStatusConsumer(ProcessedEventRepository processedEventRepository, EmailService emailService) {
        this.processedEventRepository = processedEventRepository;
        this.emailService = emailService;
    }

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 2000))
    @KafkaListener(topics = "application.status.updated", groupId = "notification-group")
    public void consume(ApplicationStatusUpdatedEvent event) {
        String eventId = event.getEventId();

        if (processedEventRepository.existsById(eventId)) {
            log.info("Duplicate status event ignored: {}", eventId);
            return;
        }

        processedEventRepository.save(new ProcessedEvent(eventId));

        if (event.getUserEmail() != null && !event.getUserEmail().isBlank()) {
            emailService.sendStatusUpdateNotification(
                    event.getUserEmail(),
                    event.getApplicationId(),
                    event.getNewStatus()
            );
        } else {
            log.warn("No email for userId={}, skipping status notification for application {}",
                    event.getUserId(), event.getApplicationId());
        }
    }
}
