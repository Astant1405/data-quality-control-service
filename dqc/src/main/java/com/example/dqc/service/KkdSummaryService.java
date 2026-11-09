package com.example.dqc.service;

import com.example.dqc.api.dto.*;
import com.example.dqc.api.mapper.KkdMapper;
import com.example.dqc.model.*;
import com.example.dqc.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KkdSummaryService {

    private final KkdRunRepository runRepository;
    private final DepartmentRepository departmentRepository;
    private final KkdMetricSnapshotRepository snapshotRepository;
    private final KkdIssueScopeService issueScopeService;
    private final DepartmentTreeService departmentTreeService;
    private final KkdIssueDeltaService deltaService;
    private final KkdMapper mapper;

    public KkdSummaryService(
            KkdRunRepository runRepository,
            DepartmentRepository departmentRepository,
            KkdMetricSnapshotRepository snapshotRepository,
            KkdIssueScopeService issueScopeService,
            DepartmentTreeService departmentTreeService,
            KkdIssueDeltaService deltaService,
            KkdMapper mapper
    ) {
        this.runRepository = runRepository;
        this.departmentRepository = departmentRepository;
        this.snapshotRepository = snapshotRepository;
        this.issueScopeService = issueScopeService;
        this.departmentTreeService = departmentTreeService;
        this.deltaService = deltaService;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public KkdSummaryResponse getSummary(Long runId, Long unitId) {
        KkdRun run = runRepository.findById(runId)
                .orElseThrow(() -> new EntityNotFoundException("Прогон не найден"));
        Department unit = departmentRepository.findById(unitId)
                .orElseThrow(() -> new EntityNotFoundException("Подразделение не найдено"));

        KkdMetricSnapshot currentSnap = getSnapshot(runId, unitId);
        KkdMetricSnapshot prevSnap = getPreviousSnapshot(run.getComparedToRunId(), unitId);

        boolean isFirstRun = run.getComparedToRunId() == null;
        Set<Long> unitIds = departmentTreeService.getSubtreeIds(unitId);

        List<KkdIssue> currentIssues = issueScopeService.findIssues(runId, unitIds);

        List<KkdIssue> previousIssues = isFirstRun ?
                Collections.emptyList() :
                issueScopeService.findIssues(run.getComparedToRunId(), unitIds);

        return new KkdSummaryResponse(
                mapper.toRunInfo(run),
                isFirstRun ? null : mapper.toRunInfo(runRepository.findById(run.getComparedToRunId()).orElseThrow()),
                mapper.toUnitInfo(unit),
                buildMetrics(currentSnap, prevSnap),
                buildSeverities(currentIssues, previousIssues, isFirstRun),
                buildDelta(run, unitIds)
        );
    }

    private List<MetricInfo> buildMetrics(KkdMetricSnapshot current, KkdMetricSnapshot previous) {
        int active = current.getActiveGoalsCount();
        BigDecimal currentCleanShare = calcShare(current.getCleanGoalsCount(), active);
        BigDecimal currentIssuesPer100 = calcShare(current.getIssuesCount(), active);

        if (previous == null) {
            return List.of(
                    new MetricInfo("clean_goals_share", "Доля корректных целей", "%", currentCleanShare, null, null, null),
                    new MetricInfo("issues_per_100_goals", "Проблем на 100 целей", "шт", currentIssuesPer100, null, null, null)
            );
        }

        BigDecimal prevCleanShare = calcShare(previous.getCleanGoalsCount(), previous.getActiveGoalsCount());
        BigDecimal prevIssuesPer100 = calcShare(previous.getIssuesCount(), previous.getActiveGoalsCount());

        BigDecimal deltaClean = currentCleanShare.subtract(prevCleanShare);
        BigDecimal deltaIssues = currentIssuesPer100.subtract(prevIssuesPer100);

        String trendClean = deltaClean.compareTo(BigDecimal.ZERO) > 0 ? "better" :
                (deltaClean.compareTo(BigDecimal.ZERO) < 0 ? "worse" : "same");
        String trendIssues = deltaIssues.compareTo(BigDecimal.ZERO) < 0 ? "better" :
                (deltaIssues.compareTo(BigDecimal.ZERO) > 0 ? "worse" : "same");

        return List.of(
                new MetricInfo("clean_goals_share", "Доля корректных целей", "%", currentCleanShare, prevCleanShare, deltaClean, trendClean),
                new MetricInfo("issues_per_100_goals", "Проблем на 100 целей", "шт", currentIssuesPer100, prevIssuesPer100, deltaIssues, trendIssues)
        );
    }

    private List<SeverityInfo> buildSeverities(List<KkdIssue> current, List<KkdIssue> previous, boolean isFirstRun) {
        Map<String, Long> curCounts = countBySeverity(current);
        Map<String, Long> prevCounts = isFirstRun ? Collections.emptyMap() : countBySeverity(previous);

        return List.of(
                buildSeverity("critical", "CRITICAL", curCounts, prevCounts, isFirstRun),
                buildSeverity("major", "MAJOR", curCounts, prevCounts, isFirstRun),
                buildSeverity("minor", "MINOR", curCounts, prevCounts, isFirstRun)
        );
    }

    private SeverityInfo buildSeverity(String code, String severityKey, Map<String, Long> current, Map<String, Long> previous, boolean isFirstRun) {
        int cur = current.getOrDefault(severityKey, 0L).intValue();
        if (isFirstRun) {
            return new SeverityInfo(code, cur, 0, 0);
        }
        int prev = previous.getOrDefault(severityKey, 0L).intValue();
        return new SeverityInfo(code, cur, prev, cur - prev);
    }

    private DeltaInfo buildDelta(KkdRun run, Set<Long> unitIds) {
        if (run.getComparedToRunId() == null) {
            return new DeltaInfo(null, null, null);
        }
        KkdIssueDeltaService.DeltaResult delta = deltaService.calculate(run.getId(), run.getComparedToRunId(), unitIds);
        return new DeltaInfo(delta.newIssues(), delta.persistingIssues(), delta.resolvedIssues());
    }


    private BigDecimal calcShare(int count, int total) {
        if (total == 0) return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        return BigDecimal.valueOf(count * 100.0)
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
    }

    private Map<String, Long> countBySeverity(List<KkdIssue> issues) {
        return issues.stream()
                .collect(Collectors.groupingBy(
                        issue -> issue.getSeverity().toUpperCase(),
                        Collectors.counting()
                ));
    }

    private KkdMetricSnapshot getSnapshot(Long runId, Long unitId) {
        return snapshotRepository.findByRunIdAndUnitId(runId, unitId)
                .orElseThrow(() -> new EntityNotFoundException("Снимок метрик не найден"));
    }

    private KkdMetricSnapshot getPreviousSnapshot(Long prevRunId, Long unitId) {
        if (prevRunId == null) return null;
        return snapshotRepository.findByRunIdAndUnitId(prevRunId, unitId).orElse(null);
    }
}