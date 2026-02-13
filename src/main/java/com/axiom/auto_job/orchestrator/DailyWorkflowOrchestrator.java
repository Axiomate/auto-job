package com.axiom.auto_job.orchestrator;

import java.io.File;
import java.util.List;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.axiom.auto_job.agent.EmailContentAgent;
import com.axiom.auto_job.agent.EmailSenderAgent;
import com.axiom.auto_job.agent.FollowUpAgent;
import com.axiom.auto_job.agent.LeadDiscoveryAgent;
import com.axiom.auto_job.agent.MatchAgent;
import com.axiom.auto_job.agent.ResumeTailorAgent;
import com.axiom.auto_job.dto.EmailRequest;
import com.axiom.auto_job.record.JobInfo;
import com.axiom.auto_job.record.ResumeTailorInput;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class DailyWorkflowOrchestrator {

    private final LeadDiscoveryAgent discovery;
    private final MatchAgent match;
    private final ResumeTailorAgent resumeTailorAgent;
    private final EmailContentAgent contentAgent;
    private final EmailSenderAgent emailSender;
    private final FollowUpAgent followUpAgent;

    private static final int DAILY_LIMIT = 300;
    private static final File BASE_RESUME = new File("resumes/base-resume.pdf");

    // -------------------- FIRST-TIME EMAILS --------------------
    @Scheduled(cron = "00 41 19 * * *", zone = "Asia/Dubai")
    public void runDaily() {

        // 1️⃣ Discover leads
        List<JobInfo> leads = discovery.execute(null);

        // 2️⃣ Filter for matches and limit daily batch
        leads.stream()
             .filter(match::execute)
             .limit(DAILY_LIMIT)
             .forEach(job -> {
                 try {
                     // 3️⃣ Tailor resume
                     ResumeTailorInput resumeInput = new ResumeTailorInput(job, BASE_RESUME);
                     File tailoredResume = resumeTailorAgent.execute(resumeInput);

                     // 4️⃣ Generate AI email content
                     var emailContent = contentAgent.execute(job);

                     // 5️⃣ Build EmailRequest with tailored resume
                     EmailRequest request = EmailRequest.from(job, emailContent)
                                                       .withResume(tailoredResume); // if EmailRequest supports this

                     // 6️⃣ Send email
                     boolean sent = emailSender.execute(request);

                     if (sent) {
                         System.out.println("Email sent to: " + job.email() + " | Company: " + job.companyName());
                     }

                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             });
    }

    // -------------------- FOLLOW-UP EMAILS --------------------
//    @Scheduled(cron = "00 40 19 * * *", zone = "Asia/Dubai")
//    public void sendFollowUps() {
//        int count = followUpAgent.execute(null);
//        System.out.println("Follow-up emails sent today: " + count);
//    }
}
