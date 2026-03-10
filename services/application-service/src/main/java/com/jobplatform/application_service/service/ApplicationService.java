package com.jobplatform.application_service.service;

import com.jobplatform.application_service.entity.Application;
import com.jobplatform.application_service.event.ApplicationCreatedEvent;
import com.jobplatform.application_service.kafka.ApplicationEventProducer;
import com.jobplatform.application_service.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ApplicationService {

    private static final Set<String> VALID_STATUSES = Set.of("APPLIED", "REVIEWING", "HIRED", "REJECTED");

    private final ApplicationRepository applicationRepository;
    private final ApplicationEventProducer eventProducer;

    public ApplicationService(ApplicationRepository applicationRepository, ApplicationEventProducer eventProducer) {
        this.applicationRepository = applicationRepository;
        this.eventProducer = eventProducer;
    }

    public Application apply(Application application) {
        if (applicationRepository.existsByUserIdAndJobId(application.getUserId(), application.getJobId())) {
            throw new IllegalArgumentException("You have already applied to this job");
        }
        application.setStatus("APPLIED");
        application.setAppliedAt(LocalDateTime.now());
        Application saved = applicationRepository.save(application);

        eventProducer.sendApplicationCreatedEvent(ApplicationCreatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .applicationId(saved.getId())
                .userId(saved.getUserId())
                .jobId(saved.getJobId())
                .userEmail(saved.getUserEmail())
                .timestamp(LocalDateTime.now())
                .build());

        return saved;
    }

    public Optional<Application> getApplication(Long id) {
        return applicationRepository.findById(id);
    }

    public List<Application> getApplicationsByUser(Long userId) {
        return applicationRepository.findByUserId(userId);
    }

    public List<Application> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    public Application updateStatus(Long id, String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Invalid status. Must be one of: " + VALID_STATUSES);
        }
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
        application.setStatus(status);
        return applicationRepository.save(application);
    }
}
