package com.example.dqc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
public class KkdRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate snapshotDate;

    private Long comparedToRunId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private OffsetDateTime startedAt;

    private OffsetDateTime completedAt;

    @Column(nullable = false, unique = true)
    private String runCode;

    public KkdRun(){}

    public Long getId(){
        return id;
    }

    public LocalDate getSnapshotDate(){
        return snapshotDate;
    }

    public String getStatus(){
        return  status;
    }

    public String getRunCode(){
        return runCode;
    }

    public Long getComparedToRunId() {
        return comparedToRunId;
    }

    public void setComparedToRunId(Long comparedToRunId) {
        this.comparedToRunId = comparedToRunId;
    }

    public static KkdRun start(String runCode, LocalDate snapshotDate, OffsetDateTime startedAt) {
        KkdRun run = new KkdRun();
        run.runCode = runCode;
        run.snapshotDate = snapshotDate;
        run.status = "RUNNING";
        run.startedAt = startedAt;
        return run;
    }

    public void complete(OffsetDateTime completedAt) {
        this.status = "COMPLETED";
        this.completedAt = completedAt;
    }

    public void fail(OffsetDateTime completedAt) {
        this.status = "FAILED";
        this.completedAt = completedAt;
    }
}
