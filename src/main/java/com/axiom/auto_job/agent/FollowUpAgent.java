package com.axiom.auto_job.agent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.axiom.auto_job.contract.Agent;
import com.axiom.auto_job.dto.EmailRequest;
import com.axiom.auto_job.entity.Lead;
import com.axiom.auto_job.record.EmailContent;
import com.axiom.auto_job.record.JobInfo;
import com.axiom.auto_job.repo.LeadRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowUpAgent implements Agent<Void, Integer> {

	private final LeadRepository leadRepository;
	private final EmailContentAgent emailContentAgent;
	private final EmailSenderAgent emailSenderAgent;

	private static final int FOLLOW_UP_DAYS = 3;

	@Override
	public Integer execute(Void input) {

		List<Lead> leadsToFollowUp = leadRepository.findAll().stream().filter(this::shouldSendFollowUp).toList();

		int count = 0;

		for (Lead lead : leadsToFollowUp) {

			// Convert Lead to JobInfo for EmailContentAgent
			JobInfo jobInfo = leadToJobInfo(lead);

			// Generate email content using AI
			EmailContent content = emailContentAgent.execute(jobInfo);

			// Build EmailRequest using the static factory
			EmailRequest req = EmailRequest.from(jobInfo, content);

			boolean sent = emailSenderAgent.execute(req);

			if (sent) {
				lead.setFollowUpSentAt(LocalDateTime.now());
				lead.setStatus("FOLLOWUP_SENT");
				leadRepository.save(lead);
				count++;
			}
		}

		return count;
	}

	private boolean shouldSendFollowUp(Lead lead) {
		if ("REPLIED".equalsIgnoreCase(lead.getStatus()) || "FOLLOWUP_SENT".equalsIgnoreCase(lead.getStatus()))
			return false;

		if (lead.getFirstEmailSentAt() == null)
			return false;

		return lead.getFirstEmailSentAt().plusDays(FOLLOW_UP_DAYS).isBefore(LocalDateTime.now());
	}

	private JobInfo leadToJobInfo(Lead lead) {
		return new JobInfo(lead.getId(), lead.getCompany(), lead.getJobTitle(),
				lead.getDescription() != null ? lead.getDescription() : "", lead.getEmail());
	}

}
