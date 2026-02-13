package com.axiom.auto_job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStats {
    private long totalLeads;
    private long sent;
    private long opened;
    private long replied;
    private long followUpScheduled;
}

