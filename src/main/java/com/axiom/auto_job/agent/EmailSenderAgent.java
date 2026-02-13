package com.axiom.auto_job.agent;

import java.time.LocalDateTime;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.axiom.auto_job.contract.Agent;
import com.axiom.auto_job.dto.EmailRequest;
import com.axiom.auto_job.entity.EmailLog;
import com.axiom.auto_job.repo.EmailLogRepository;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailSenderAgent implements Agent<EmailRequest, Boolean> {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    @Override
    public Boolean execute(EmailRequest request) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(request.to());
            helper.setSubject(request.subject());
            helper.setText(request.body());

            if (request.resume() != null && request.resume().exists()) {
                helper.addAttachment("resume.pdf", request.resume());
            }

//            mailSender.send(message);

            // ✅ SAVE TO DB
            EmailLog log = new EmailLog();
            log.setLeadId(request.companyId());
            log.setSubject(request.subject());
            log.setBody(request.body());
            log.setType("INITIAL");
            log.setStatus("SENT");
            log.setSentAt(LocalDateTime.now());

            emailLogRepository.save(log);

            return true;

        } catch (Exception e) {

            EmailLog log = new EmailLog();
            log.setLeadId(request.companyId());
            log.setSubject(request.subject());
            log.setBody(request.body());
            log.setType("INITIAL");
            log.setStatus("FAILED");

            emailLogRepository.save(log);

            return false;
        }
    }
}
