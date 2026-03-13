package com.jobplatform.application_service.kafka;

import com.jobplatform.application_service.event.ApplicationCreatedEvent;
import com.jobplatform.application_service.event.ApplicationStatusUpdatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ApplicationEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendApplicationCreatedEvent(ApplicationCreatedEvent event) {
        kafkaTemplate.send("application.created", event);
    }

    public void sendApplicationStatusUpdatedEvent(ApplicationStatusUpdatedEvent event) {
        kafkaTemplate.send("application.status.updated", event);
    }
}
