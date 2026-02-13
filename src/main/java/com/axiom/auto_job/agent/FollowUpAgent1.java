package com.axiom.auto_job.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class FollowUpAgent1 extends BaseAiAgent {

    public FollowUpAgent1(ChatClient chatClient) {
        super(chatClient);
    }

    public String generateFollowUp(String previousEmail) {

        String prompt = """
        Write a polite follow-up email for this previous message:

        %s

        Keep short and professional.
        """.formatted(previousEmail);

        return ask(prompt);
    }
}
