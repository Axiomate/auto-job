package com.axiom.auto_job.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    private Lead lead;

    private String subject;
    @Column(columnDefinition = "TEXT")
    private String body;
    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    private String attachmentPath;

    private LocalDateTime sentAt = LocalDateTime.now();
    private Boolean opened = false;
    private Boolean replied = false;
    private Boolean followUp = false;
}
