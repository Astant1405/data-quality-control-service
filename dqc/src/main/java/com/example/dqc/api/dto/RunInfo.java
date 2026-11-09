package com.example.dqc.api.dto;

import java.time.LocalDate;

public record RunInfo(
        String id, LocalDate snapshotDate
) {
}
