package com.axiom.auto_job.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.axiom.auto_job.entity.EmailLog;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
}
