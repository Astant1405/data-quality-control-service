package com.example.dqc.service.delta;

import com.example.dqc.domain.ObjectType;

public record IssueKey(
        String ruleCode,
        ObjectType objectType,
        Long objectId
) {
}
