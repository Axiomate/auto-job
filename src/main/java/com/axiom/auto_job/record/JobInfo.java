package com.axiom.auto_job.record;

public record JobInfo(
	    Long leadId,
	    String companyName,
	    String jobTitle,
	    String description,
	    String email
	) {}
