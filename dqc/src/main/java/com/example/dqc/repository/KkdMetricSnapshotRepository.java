package com.example.dqc.repository;

import com.example.dqc.model.KkdMetricSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KkdMetricSnapshotRepository extends JpaRepository<KkdMetricSnapshot, Long> {
    Optional<KkdMetricSnapshot> findByRunIdAndUnitId(Long runId, Long unitId);
}
