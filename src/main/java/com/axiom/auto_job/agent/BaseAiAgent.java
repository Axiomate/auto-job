package com.axiom.auto_job.agent;

import org.springframework.ai.chat.client.ChatClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseAiAgent {

    protected final ChatClient chatClient;

    protected String ask(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    protected <T> T askStructured(String prompt, Class<T> type) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .entity(type);
    }
}

