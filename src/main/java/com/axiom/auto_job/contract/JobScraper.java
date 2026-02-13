package com.axiom.auto_job.contract;

import java.util.List;

import com.axiom.auto_job.record.JobInfo;

@FunctionalInterface
public interface JobScraper {

    List<JobInfo> scrape();

}

