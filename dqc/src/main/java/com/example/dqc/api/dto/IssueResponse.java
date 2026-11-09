package com.example.dqc.api.dto;

import java.time.OffsetDateTime;

public record IssueResponse(
        Long id,
        String ruleCode,
        String severity,
        String objectType,
        Long objectId,
        String description,
        OffsetDateTime createdAt
) {
}
