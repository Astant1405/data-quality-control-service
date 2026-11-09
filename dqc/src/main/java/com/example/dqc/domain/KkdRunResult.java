package com.example.dqc.domain;

public record KkdRunResult(
        Long runId,
        String runCode,
        String status,
        int issuesCount,
        Integer newIssues,
        Integer persistingIssues,
        Integer resolvedIssues
) {
}
