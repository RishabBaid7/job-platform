package com.jobplatform.job_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;  
    public JobEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendJobCreatedEvent(String message) {
        kafkaTemplate.send("job.created", message);
    }
}
