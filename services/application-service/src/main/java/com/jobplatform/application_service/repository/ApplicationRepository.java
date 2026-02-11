package com.jobplatform.application_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jobplatform.application_service.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUserId(Long userId);
}
