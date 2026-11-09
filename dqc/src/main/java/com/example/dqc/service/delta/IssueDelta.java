package com.example.dqc.service.delta;

public record IssueDelta(
        Integer newIssues,
        Integer persistingIssues,
        Integer resolvedIssues
) {
}
