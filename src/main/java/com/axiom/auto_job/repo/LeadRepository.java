package com.axiom.auto_job.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.axiom.auto_job.entity.Lead;

import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

	// Find leads eligible for follow-up
	List<Lead> findByStatus(String status);

	default List<Lead> findLeadsForFollowUp(int followUpDays) {
		return findAll().stream()
				.filter(lead -> !"REPLIED".equalsIgnoreCase(lead.getStatus())
						&& !"FOLLOWUP_SENT".equalsIgnoreCase(lead.getStatus()) && lead.getFirstEmailSentAt() != null
						&& lead.getFirstEmailSentAt().isBefore(java.time.LocalDateTime.now().minusDays(followUpDays)))
				.toList();
	}

	long countByStatus(String status);

	long countByFollowUpSentAtIsNullAndFirstEmailSentAtIsNotNull();

	// Custom pagination query for leads with optional status filter
	@Query(value = "SELECT * FROM lead l WHERE (:status IS NULL OR l.status = :status) ORDER BY l.first_email_sent_at DESC LIMIT :size OFFSET :offset", nativeQuery = true)
	List<Lead> findLeads(@Param("status") String status, @Param("offset") int offset, @Param("size") int size);

}
