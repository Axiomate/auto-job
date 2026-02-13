package com.axiom.auto_job.entity;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long leadId;

    @Column(columnDefinition = "TEXT")
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String type;    // INITIAL, FOLLOW_UP
    private String status;  // SENT, FAILED, OPENED

    private LocalDateTime sentAt;
    private LocalDateTime openedAt;
    private LocalDateTime repliedAt;
}

