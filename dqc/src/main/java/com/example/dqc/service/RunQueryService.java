package com.example.dqc.service;

import com.example.dqc.api.dto.RunResponse;
import com.example.dqc.model.KkdRun;
import com.example.dqc.repository.KkdIssueRepository;
import com.example.dqc.repository.KkdRunRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RunQueryService {

    private final KkdRunRepository runRepository;
    private final KkdIssueRepository issueRepository;

    public RunQueryService(KkdRunRepository runRepository, KkdIssueRepository issueRepository) {
        this.runRepository = runRepository;
        this.issueRepository = issueRepository;
    }

    @Transactional(readOnly = true)
    public List<RunResponse> getRuns() {
        List<KkdRun> runs = runRepository.findAllByOrderByStartedAtDesc();

        Map<Long, Integer> issuesCounts = issueRepository.countIssuesGroupedByRun()
                .stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> ((Number) row[1]).intValue()
                ));

        Map<Long, String> runCodes = runs.stream()
                .collect(Collectors.toMap(KkdRun::getId, KkdRun::getRunCode));

        return runs.stream().map(run -> new RunResponse(
                run.getId(),
                run.getRunCode(),
                run.getSnapshotDate(),
                run.getComparedToRunId() == null ? null : runCodes.get(run.getComparedToRunId()),
                run.getStatus(),
                issuesCounts.getOrDefault(run.getId(), 0)
        )).toList();
    }
}
