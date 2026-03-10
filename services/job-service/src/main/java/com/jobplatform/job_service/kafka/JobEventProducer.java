package com.jobplatform.job_service.kafka;

import com.jobplatform.job_service.event.JobCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobEventProducer {

    private final KafkaTemplate<String, JobCreatedEvent> kafkaTemplate;

    public JobEventProducer(KafkaTemplate<String, JobCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendJobCreatedEvent(JobCreatedEvent event) {
        kafkaTemplate.send("job.created", event);
    }
}
