package com.axiom.auto_job.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axiom.auto_job.entity.Email;
import com.axiom.auto_job.entity.Lead;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    List<Email> findByLead(Lead lead);
}
