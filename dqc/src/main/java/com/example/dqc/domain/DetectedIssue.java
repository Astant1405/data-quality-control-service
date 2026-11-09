package com.example.dqc.domain;

public record DetectedIssue(
        String ruleCode,
        ObjectType objectType,
        Long objectId,
        String description
) {
}
