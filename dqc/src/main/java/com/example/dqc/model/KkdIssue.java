package com.example.dqc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import java.time.OffsetDateTime;

@Entity
public class KkdIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long runId;

    @Column(nullable = false)
    private Long ruleId;

    private Long goalId;

    private Long keyResultId;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    public KkdIssue(){}

    public Long getId(){
        return id;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public Long getGoalId() {
        return goalId;
    }

    public Long getKeyResultId() {
        return keyResultId;
    }

    public String getSeverity(){
        return severity;
    }

    public String getDescription(){
        return description;
    }

    public OffsetDateTime getCreatedAt(){
        return createdAt;
    }

    public static KkdIssue create(
            Long runId,
            Long ruleId,
            Long goalId,
            Long keyResultId,
            String severity,
            String description,
            OffsetDateTime createdAt
    ) {
        KkdIssue issue = new KkdIssue();
        issue.runId = runId;
        issue.ruleId = ruleId;
        issue.goalId = goalId;
        issue.keyResultId = keyResultId;
        issue.severity = severity;
        issue.description = description;
        issue.createdAt = createdAt;

        return issue;
    }
}
