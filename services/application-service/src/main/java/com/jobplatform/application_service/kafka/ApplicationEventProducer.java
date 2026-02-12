package com.jobplatform.application_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.jobplatform.application_service.event.ApplicationCreatedEvent;


@Component
public class ApplicationEventProducer {
    private final KafkaTemplate<String, ApplicationCreatedEvent> kafkaTemplate;
    
    public ApplicationEventProducer(KafkaTemplate<String, ApplicationCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendApplicationCreatedEvent(ApplicationCreatedEvent event) {
        kafkaTemplate.send("application.created", event); 
    }
}
