package com.axiom.auto_job.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axiom.auto_job.entity.AiLog;
import com.axiom.auto_job.entity.Lead;

import java.util.List;

@Repository
public interface AiLogRepository extends JpaRepository<AiLog, Long> {
    List<AiLog> findByLead(Lead lead);
}

