package com.jobplatform.job_service.service;

import com.jobplatform.job_service.entity.Job;
import com.jobplatform.job_service.event.JobCreatedEvent;
import com.jobplatform.job_service.kafka.JobEventProducer;
import com.jobplatform.job_service.repository.JobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final JobEventProducer jobEventProducer;

    public JobService(JobRepository jobRepository, JobEventProducer jobEventProducer) {
        this.jobRepository = jobRepository;
        this.jobEventProducer = jobEventProducer;
    }

    public Job createJob(Job job) {
        job.setCreatedAt(LocalDateTime.now());
        Job saved = jobRepository.save(job);

        jobEventProducer.sendJobCreatedEvent(JobCreatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .jobId(saved.getId())
                .title(saved.getTitle())
                .companyName(saved.getCompanyName())
                .location(saved.getLocation())
                .salary(saved.getSalary())
                .timestamp(LocalDateTime.now())
                .build());

        return saved;
    }

    public Page<Job> getAllJobs(String title, String location, Double minSalary, Double maxSalary, Pageable pageable) {
        return jobRepository.search(title, location, minSalary, maxSalary, pageable);
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public Job updateJob(Long id, Job updated) {
        Job existing = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setCompanyName(updated.getCompanyName());
        existing.setLocation(updated.getLocation());
        existing.setSalary(updated.getSalary());
        return jobRepository.save(existing);
    }

    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new RuntimeException("Job not found with id: " + id);
        }
        jobRepository.deleteById(id);
    }
}
