package com.example.innova.repository;

import com.example.innova.model.JobLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobLogRepository extends JpaRepository<JobLog, Long> {
    //JobLog için özelleştirilmiş SQL komutları yazılabilir.
    List<JobLog> findByUserId(Long userId);




}
