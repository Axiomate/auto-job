package com.axiom.auto_job.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    private Lead lead;

    private String companyName;
    private String jobTitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String source; // LinkedIn, Indeed, Bayt, etc
    private String url;

    private LocalDateTime scrapedAt = LocalDateTime.now();
}

