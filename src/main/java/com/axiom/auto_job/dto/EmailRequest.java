package com.axiom.auto_job.dto;

import java.io.File;

import com.axiom.auto_job.record.EmailContent;
import com.axiom.auto_job.record.JobInfo;

public record EmailRequest(
        String to,
        String subject,
        String body,
        File resume,
        Long companyId
) {

    public static EmailRequest from(JobInfo job, EmailContent content) {
        return new EmailRequest(
                job.email(),
                content.subject(),
                content.body() + "\n\n" + content.coverLetter(),
                new File("resumes/" + sanitizeFileName(job.companyName()) + "-resume.pdf"),
                job.leadId()
        );
    }

    // Optional: sanitize file name to remove illegal chars
    private static String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }
    
    public EmailRequest withResume(File resumeFile) {
        return new EmailRequest(this.to, this.subject, this.body, resumeFile, this.companyId);
    }

}
