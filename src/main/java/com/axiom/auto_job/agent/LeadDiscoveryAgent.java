package com.axiom.auto_job.agent;

import java.util.List;

import org.springframework.stereotype.Service;

import com.axiom.auto_job.contract.Agent;
import com.axiom.auto_job.contract.JobScraper;
import com.axiom.auto_job.record.JobInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeadDiscoveryAgent
        implements Agent<Void, List<JobInfo>> {

    private final List<JobScraper> scrapers;

    @Override
    public List<JobInfo> execute(Void input) {

        return scrapers.stream()
                .parallel()
                .flatMap(s -> s.scrape().stream())
                .distinct()
                .toList();
    }
}

