package com.axiom.auto_job.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.axiom.auto_job.dto.DashboardStats;
import com.axiom.auto_job.entity.Lead;
import com.axiom.auto_job.repo.LeadRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DashboardController {

	private final LeadRepository leadRepository;

	// -------------------- 1. Stats --------------------
	@GetMapping("/api/dashboard/stats")
	public DashboardStats getStats() {

		long totalLeads = leadRepository.count();
		long sent = leadRepository.countByStatus("SENT");
		long opened = leadRepository.countByStatus("OPENED");
		long replied = leadRepository.countByStatus("REPLIED");
		long followUpScheduled = leadRepository.countByFollowUpSentAtIsNullAndFirstEmailSentAtIsNotNull();

		return new DashboardStats(totalLeads, sent, opened, replied, followUpScheduled);
	}

	// -------------------- 2. Leads List --------------------
	@GetMapping("/api/dashboard/leads")
	public List<Lead> getLeads(@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		// Simple pagination
		int offset = page * size;
		return leadRepository.findLeads(status, offset, size);
	}
}
