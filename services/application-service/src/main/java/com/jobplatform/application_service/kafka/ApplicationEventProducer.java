package com.jobplatform.application_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class ApplicationEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    public ApplicationEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendApplicationCreatedEvent(String event) {
        kafkaTemplate.send("application.created", event); 
    }
}
