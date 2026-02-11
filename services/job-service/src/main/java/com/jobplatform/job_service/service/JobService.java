package com.jobplatform.job_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jobplatform.job_service.entity.Job;
import com.jobplatform.job_service.kafka.JobEventProducer;
import com.jobplatform.job_service.repository.JobRepository;

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
        String event = "Job created: id=" 
                   + saved.getId()
                   + ", title=" 
                   + saved.getTitle();
        jobEventProducer.sendJobCreatedEvent(event);
        return saved;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @SuppressWarnings("null")
    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }
}
