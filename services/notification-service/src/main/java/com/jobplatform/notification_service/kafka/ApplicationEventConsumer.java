package com.jobplatform.notification_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import com.jobplatform.notification_service.entity.ProcessedEvent;
import com.jobplatform.notification_service.repository.ProcessedEventRepository;

@Component
public class ApplicationEventConsumer {

    private final ProcessedEventRepository processedEventRepository;
    
    public ApplicationEventConsumer(ProcessedEventRepository processedEventRepository) {
        this.processedEventRepository = processedEventRepository;
    }

    @SuppressWarnings("null")
    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 2000))
    @KafkaListener(topics = "application.created", groupId = "notification-group") 
    public void consume(String message) {
        // String eventId = event.getEventId();
        String eventId = message; 

        if(processedEventRepository.existsById(eventId)) {
            System.out.println("Duplicate event ignored: " + eventId);
            return;
        }

        System.out.println("Processing application event:" + eventId);
        
        processedEventRepository.save(new ProcessedEvent(eventId));
    }
}
