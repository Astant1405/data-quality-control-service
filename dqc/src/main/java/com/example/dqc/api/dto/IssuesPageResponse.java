package com.example.dqc.api.dto;

import java.util.List;

public record IssuesPageResponse(
        List<IssueResponse> issues,
        int limit,
        int offset,
        long total
) {
}
