package com.example.dqc.api.dto;

import java.util.List;

public record KkdSummaryResponse(
        RunInfo run,
        RunInfo comparedTo,
        UnitInfo unit,
        List<MetricInfo> metrics,
        List<SeverityInfo> issuesBySeverity,
        DeltaInfo delta

) {
}
