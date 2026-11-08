package com.example.dqc.dto;

public record DetectedIssue(
        String ruleCode,
        ObjectType objectType,
        Long objectId,
        String description
) {
}
