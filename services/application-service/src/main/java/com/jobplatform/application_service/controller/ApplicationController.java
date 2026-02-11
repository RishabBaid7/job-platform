package com.jobplatform.application_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;

import com.jobplatform.application_service.entity.Application;
import com.jobplatform.application_service.service.ApplicationService;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public Application apply(@RequestBody Application application) {
        return applicationService.apply(application);
    }

    @GetMapping("/{id}")
    public Optional<Application> getApplication(@PathVariable Long id) {
        return applicationService.getApplication(id);
    }

    @GetMapping("/user/{userId}")
    public List<Application> getUserApplications(@PathVariable Long userId) {
        return applicationService.getApplicationsByUser(userId);
    }
}
