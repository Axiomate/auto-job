package com.axiom.auto_job.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class CompanyResearchAgent extends BaseAiAgent {

    public CompanyResearchAgent(ChatClient chatClient) {
        super(chatClient);
    }

    public String summarize(String company, String jd) {

        String prompt = """
        Summarize this company and role in 3 concise lines for email personalization.

        Company: %s
        Job Description: %s
        """.formatted(company, jd);

        return ask(prompt);
    }
}

