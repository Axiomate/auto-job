package com.axiom.auto_job.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axiom.auto_job.entity.JobDetails;
import com.axiom.auto_job.entity.Lead;

import java.util.List;

@Repository
public interface JobDetailsRepository extends JpaRepository<JobDetails, Long> {
    List<JobDetails> findByLead(Lead lead);
}

