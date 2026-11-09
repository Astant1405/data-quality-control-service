package com.example.dqc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
public class KkdMetricSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "run_id", nullable = false)
    private Long runId;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Column(name = "unit_name", nullable = false)
    private String unitName;

    @Column(name = "active_goals_count", nullable = false)
    private int activeGoalsCount;

    @Column(name = "clean_goals_count", nullable = false)
    private int cleanGoalsCount;

    @Column(name = "goals_with_issues_count", nullable = false)
    private int goalsWithIssuesCount;

    @Column(name = "issues_count", nullable = false)
    private int issuesCount;

    public KkdMetricSnapshot(){}

    public static KkdMetricSnapshot create(
            Long runId,
            Long unitId,
            String unitName,
            int activeGoals,
            int cleanGoals,
            int goalsWithIssues,
            int issues
    ) {
        KkdMetricSnapshot entity = new KkdMetricSnapshot();

        entity.runId = runId;
        entity.unitId = unitId;
        entity.unitName = unitName;
        entity.activeGoalsCount = activeGoals;
        entity.cleanGoalsCount = cleanGoals;
        entity.goalsWithIssuesCount = goalsWithIssues;
        entity.issuesCount = issues;

        return entity;
    }

    public Long getRunId() {
        return runId;
    }

    public String getUnitName() {
        return unitName;
    }

    public int getActiveGoalsCount() {
        return activeGoalsCount;
    }

    public int getCleanGoalsCount() {
        return cleanGoalsCount;
    }

    public int getGoalsWithIssuesCount() {
        return goalsWithIssuesCount;
    }

    public int getIssuesCount() {
        return issuesCount;
    }
}
