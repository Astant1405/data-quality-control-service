package com.example.dqc.api.dto;

import java.time.LocalDate;

public record RunResponse(
        Long id,
        String runCode,
        LocalDate snapshotDate,
        String comparedToRunCode,
        String status,
        int issuesCount
) {
}
