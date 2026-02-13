package com.axiom.auto_job.agent.helper;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiHelper {

    private final ChatClient chatClient;

    public <T> T ask(String prompt, Class<T> type) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .entity(type);
    }
}

