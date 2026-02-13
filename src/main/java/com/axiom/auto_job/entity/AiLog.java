package com.axiom.auto_job.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    private Lead lead;

    private String agentName; // EmailContentAgent, ResumeTailorAgent, etc

    @Column(columnDefinition = "TEXT")
    private String prompt;

    @Column(columnDefinition = "TEXT")
    private String output;

    private LocalDateTime createdAt = LocalDateTime.now();
}

