package com.example.dqc.domain;

import java.time.LocalDate;

public record StartKkdRunRequest(
        String runCode,
        LocalDate snapshotDate,
        String comparedToRunCode
) {
}
