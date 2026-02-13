package com.axiom.auto_job.agent;

import java.util.List;

import org.springframework.stereotype.Service;

import com.axiom.auto_job.contract.Agent;
import com.axiom.auto_job.record.JobInfo;

@Service
public class MatchAgent implements Agent<JobInfo, Boolean> {

	private static final List<String> KEYWORDS = List.of("Java", "Spring Boot", "Spring MVC", "AI", "OAuth",
			"JWT", "Microservices", "AWS", "Git", "Cloud", "REST", "SOAP", "Angular", "Typescript", "Docker", "Kubernetes",
			"CI/CD", "Kafka", "Redis", "PostgreSQL","MySQL", "SQL", "Agentic AI", "LLMs", "LangChain");

	@Override
	public Boolean execute(JobInfo job) {

		if (!isUae(job))
			return false;

		double score = calculateScore(job.description());

		return score >= 6.5;
	}

	private boolean isUae(JobInfo job) {
		return job.description().toLowerCase().contains("uae")
				|| job.description().toLowerCase().contains("united arab emirates")
				|| job.description().toLowerCase().contains("dubai")
				|| job.description().toLowerCase().contains("abu dhabi")
				|| job.description().toLowerCase().contains("sharjah")
				|| job.description().toLowerCase().contains("al ain")
				|| job.description().toLowerCase().contains("ajman");
	}

	private double calculateScore(String text) {

		text = text.toLowerCase();

		long matches = KEYWORDS.stream().filter(text::contains).count();

		return (matches * 10.0) / KEYWORDS.size();
	}
}
