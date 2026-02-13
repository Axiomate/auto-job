package com.axiom.auto_job.agent.scraper;

import java.util.List;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import com.axiom.auto_job.contract.JobScraper;
import com.axiom.auto_job.record.JobInfo;

@Service
public class IndeedScraper implements JobScraper {

    @Override
    public List<JobInfo> scrape() {

        try {
            var doc = Jsoup.connect(
                    "https://ae.indeed.com/jobs?q=java+spring+boot&l=UAE"
            ).get();

            return doc.select(".job_seen_beacon")
                    .stream()
                    .map(e -> new JobInfo(
                            null,
                            e.select(".companyName").text(),
                            e.select(".jobTitle").text(),
                            e.select(".job-snippet").text(),
                            "hr@" + e.select(".companyName").text().toLowerCase() + ".com"
                    ))
                    .toList();

        } catch (Exception e) {
            return List.of();
        }
    }
}

