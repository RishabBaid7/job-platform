package com.jobplatform.job_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobplatform.job_service.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
    
}
