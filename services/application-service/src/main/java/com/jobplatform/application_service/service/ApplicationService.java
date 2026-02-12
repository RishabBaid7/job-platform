package com.jobplatform.application_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jobplatform.application_service.entity.Application;
import com.jobplatform.application_service.event.ApplicationCreatedEvent;
import com.jobplatform.application_service.kafka.ApplicationEventProducer;
import com.jobplatform.application_service.repository.ApplicationRepository;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationEventProducer eventProducer; 

    public ApplicationService(ApplicationRepository applicationRepository, ApplicationEventProducer eventProducer) {
        this.applicationRepository = applicationRepository;
        this.eventProducer = eventProducer;
    }

    public Application apply(Application application) {
        application.setStatus("APPLIED");
        application.setAppliedAt(LocalDateTime.now());

        Application saved = applicationRepository.save(application);

        ApplicationCreatedEvent event = ApplicationCreatedEvent.builder()
            .eventId(UUID.randomUUID().toString())
            .applicationId(saved.getId())
            .userId(saved.getUserId())
            .jobId(saved.getJobId())
            .timestamp(LocalDateTime.now())
            .build();

        // String event = "Application created: userId=" 
        //            + saved.getUserId() 
        //            + ", jobId=" 
        //            + saved.getJobId();
        eventProducer.sendApplicationCreatedEvent(event);
        return saved;
    }

    @SuppressWarnings("null")
    public Optional<Application> getApplication(Long id) {
        return applicationRepository.findById(id);
    }

    public List<Application> getApplicationsByUser(Long userId) {
        return applicationRepository.findByUserId(userId);
    }
}
