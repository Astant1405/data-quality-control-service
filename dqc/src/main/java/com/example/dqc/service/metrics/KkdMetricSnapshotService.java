package com.example.dqc.service.metrics;

import com.example.dqc.domain.MetricSnapshotDto;
import com.example.dqc.model.KkdMetricSnapshot;
import com.example.dqc.repository.KkdMetricSnapshotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KkdMetricSnapshotService {

    private final KkdMetricSnapshotRepository repository;

    public KkdMetricSnapshotService(KkdMetricSnapshotRepository repository) {
        this.repository = repository;
    }

    public void save(Long runId, List<MetricSnapshotDto> snapshots) {
        List<KkdMetricSnapshot> entities =
                snapshots.stream()
                        .map(snapshot ->
                                KkdMetricSnapshot.create(
                                        runId,
                                        snapshot.unitId(),
                                        snapshot.unitName(),
                                        snapshot.activeGoalsCount(),
                                        snapshot.cleanGoalsCount(),
                                        snapshot.goalsWithIssuesCount(),
                                        snapshot.issuesCount()
                                )
                        )
                        .toList();

        repository.saveAll(entities);
    }
}
