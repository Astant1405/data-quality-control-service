package com.example.dqc.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeltaInfo(
        @JsonProperty("new") Integer newIssues,
        @JsonProperty("persisting") Integer persistingIssues,
        @JsonProperty("resolved") Integer resolvedIssues
) {
}
