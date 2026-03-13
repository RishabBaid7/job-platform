package com.jobplatform.notification_service.kafka;

import com.jobplatform.notification_service.entity.ProcessedEvent;
import com.jobplatform.notification_service.event.JobCreatedEvent;
import com.jobplatform.notification_service.repository.ProcessedEventRepository;
import com.jobplatform.notification_service.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
public class JobEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(JobEventConsumer.class);

    private final ProcessedEventRepository processedEventRepository;
    private final EmailService emailService;

    public JobEventConsumer(ProcessedEventRepository processedEventRepository, EmailService emailService) {
        this.processedEventRepository = processedEventRepository;
        this.emailService = emailService;
    }

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 2000))
    @KafkaListener(topics = "job.created", groupId = "notification-group")
    public void consume(JobCreatedEvent event) {
        String eventId = event.getEventId();

        if (processedEventRepository.existsById(eventId)) {
            log.info("Duplicate job event ignored: {}", eventId);
            return;
        }

        processedEventRepository.save(new ProcessedEvent(eventId));

        if (event.getPostedByEmail() != null && !event.getPostedByEmail().isBlank()) {
            emailService.sendJobPostedConfirmation(
                    event.getPostedByEmail(),
                    event.getJobId(),
                    event.getTitle(),
                    event.getCompanyName()
            );
        } else {
            log.warn("No recruiter email for jobId={}, skipping job confirmation email", event.getJobId());
        }
    }
}
