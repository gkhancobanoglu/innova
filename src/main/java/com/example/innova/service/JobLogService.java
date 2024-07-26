package com.example.innova.service;

import com.example.innova.model.JobLog;
import com.example.innova.repository.JobLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobLogService {

    private final JobLogRepository jobLogRepository;

    public JobLogService(JobLogRepository jobLogRepository) {
        this.jobLogRepository = jobLogRepository;
    }

    public List<JobLog> getAllJobLogs() {
        return jobLogRepository.findAll();
    }

    public List<JobLog> getJobLogsByUserId(Long userId) {
        return jobLogRepository.findByUserId(userId);
    }
}
