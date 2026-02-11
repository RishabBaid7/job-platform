package com.jobplatform.notification_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobplatform.notification_service.entity.ProcessedEvent;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {

}
