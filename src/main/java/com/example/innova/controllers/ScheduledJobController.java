package com.example.innova.controllers;

import com.example.innova.model.JobLog;
import com.example.innova.service.JobLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scheduled")
public class ScheduledJobController {

    private final JobLogService jobLogService;

    public ScheduledJobController(JobLogService jobLogService) {
        this.jobLogService = jobLogService;
    }

    @GetMapping("/logs")
    public List<JobLog> getAllJobLogs() {
        return jobLogService.getAllJobLogs();
    }

    @GetMapping("/logs/{userId}")
    public List<JobLog> getJobLogsByUserId(@PathVariable Long userId) {
        return jobLogService.getJobLogsByUserId(userId);
    }
}
