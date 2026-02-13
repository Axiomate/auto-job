package com.axiom.auto_job.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.axiom.auto_job.agent.helper.AiHelper;
import com.axiom.auto_job.contract.Agent;
import com.axiom.auto_job.entity.JobDetails;
import com.axiom.auto_job.record.EmailContent;
import com.axiom.auto_job.record.JobInfo;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class EmailContentAgent implements Agent<JobInfo, EmailContent> {

    private final AiHelper ai;

    @Override
    public EmailContent execute(JobInfo job) {
        return ai.ask(buildPrompt(job), EmailContent.class);
    }

    private String buildPrompt(JobInfo job) {
        return """
            Generate:
            - subject
            - email body
            - cover letter

            Company: %s
            JD: %s
            Stack: Java 21, Spring Boot, Angular
            """.formatted(job.companyName(), job.description());
    }

}
