package com.jobplatform.job_service.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String companyName;
    private String location;
    private Double salary;

    private LocalDateTime createdAt;
}