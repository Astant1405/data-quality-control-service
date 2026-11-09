package com.example.dqc.api.dto;

import java.math.BigDecimal;

public record MetricResponse(
        int activeGoals,
        int cleanGoals,
        int goalsWithIssues,
        int issuesCount,
        BigDecimal cleanGoalsShare,
        BigDecimal issuesPer100Goals
) {
}
