package com.example.dqc.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record RunInfo(
        @JsonProperty("id") String id,
        @JsonProperty("snapshot_date") LocalDate snapshotDate
) {
}
