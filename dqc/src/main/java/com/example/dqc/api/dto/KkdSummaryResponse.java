package com.example.dqc.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KkdSummaryResponse(
        RunInfo run,
        @JsonProperty("compared_to") RunInfo comparedTo,
        UnitInfo unit,
        List<MetricInfo> metrics,
        @JsonProperty("issues_by_severity") List<SeverityInfo> issuesBySeverity,
        DeltaInfo delta

) {
}
