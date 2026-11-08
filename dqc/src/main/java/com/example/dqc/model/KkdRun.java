package com.example.dqc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Date;

@Data
@Entity
public class KkdRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date snapshotDate;

    private Long comparedToRunId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private OffsetDateTime startedAt;

    private OffsetDateTime completedAt;

    @Column(nullable = false, unique = true)
    private String runCode;
}
