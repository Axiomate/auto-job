package com.axiom.auto_job.orchestrator;

import org.springframework.stereotype.Service;

import com.axiom.auto_job.agent.EmailContentAgent;
import com.axiom.auto_job.record.EmailContent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkflowOrchestrator {

    private final EmailContentAgent emailAgent;

//    public void run() {
//
//        EmailContent content =
//                emailAgent.generate("Careem", "Looking for Java Spring Boot engineer");
//
//        System.out.println(content.subject());
//        System.out.println(content.body());
//    }
}


