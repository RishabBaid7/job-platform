package com.jobplatform.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobplatform.user_service.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
