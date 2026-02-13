package com.axiom.auto_job.agent;

import java.util.List;

import org.springframework.stereotype.Service;

import com.axiom.auto_job.contract.Agent;
import com.axiom.auto_job.entity.Lead;

@Service
public class LinkedInDiscoveryAgent implements Agent<Void, List<Lead>> {

    @Override
    public List<Lead> execute(Void input) {
        // TODO: Playwright scraping
        return List.of();
    }
}

