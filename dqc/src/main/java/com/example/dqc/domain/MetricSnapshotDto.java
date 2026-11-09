package com.example.dqc.domain;

public record MetricSnapshotDto(
        Long unitId,
        String unitName,
        int activeGoalsCount,
        int cleanGoalsCount,
        int goalsWithIssuesCount,
        int issuesCount
) {
}
