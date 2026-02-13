package com.axiom.auto_job.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leads")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lead {

    @Id
    @GeneratedValue
    private Long id;

    private String company;
    private String email;
    private String jobTitle;
    private String Description;
    private Double score;

    private String status; // SENT, OPENED, REPLIED, FOLLOWUP_SENT
    private LocalDateTime firstEmailSentAt;
    private LocalDateTime followUpSentAt;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

}

