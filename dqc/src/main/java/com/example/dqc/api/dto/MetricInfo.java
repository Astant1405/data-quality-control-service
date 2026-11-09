package com.example.dqc.api.dto;

import java.math.BigDecimal;

public record MetricInfo(
        String key,
        String title,
        String unit,
        BigDecimal value,
        BigDecimal previous,
        BigDecimal delta,
        String trend
) {
}
