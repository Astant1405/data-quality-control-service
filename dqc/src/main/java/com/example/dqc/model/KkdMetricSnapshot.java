package com.example.dqc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class KkdMetricSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id")
    @Column(nullable = false, unique = true)
    private KkdRun runId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    @Column(nullable = false, unique = true)
    private Department unitId;

    @Column(nullable = false, unique = true)
    private String metricKey;

    @Column(nullable = false)
    private BigDecimal metricValue;
}
