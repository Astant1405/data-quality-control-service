package com.example.dqc.service;

import com.example.dqc.domain.*;
import com.example.dqc.engine.KkdRuleEngine;
import com.example.dqc.model.KkdRun;
import com.example.dqc.model.KkdRule;
import com.example.dqc.model.KkdIssue;
import com.example.dqc.repository.*;
import com.example.dqc.service.delta.IssueDelta;
import com.example.dqc.service.delta.IssueKey;
import com.example.dqc.service.delta.KkdIssueDeltaCalculator;
import com.example.dqc.service.delta.KkdPreviousIssuesLoader;
import com.example.dqc.service.metrics.KkdMetricCalculator;
import com.example.dqc.service.metrics.KkdMetricSnapshotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class KkdRunService {

    private final KkdDataLoader dataLoader;
    private final KkdRuleEngine ruleEngine;
    private final KkdRuleRepository ruleRepository;
    private final KkdRunRepository runRepository;
    private final KkdIssueRepository issueRepository;
    private final KkdPreviousIssuesLoader previousIssuesLoader;
    private final KkdIssueDeltaCalculator deltaCalculator;
    private final KkdMetricCalculator metricCalculator;
    private final KkdMetricSnapshotService metricSnapshotService;
    private final DepartmentRepository departmentRepository;

    public KkdRunService(
            KkdDataLoader dataLoader,
            KkdRuleEngine ruleEngine,
            KkdRuleRepository ruleRepository,
            KkdRunRepository runRepository,
            KkdIssueRepository issueRepository,
            KkdPreviousIssuesLoader previousIssuesLoader,
            KkdIssueDeltaCalculator deltaCalculator,
            KkdMetricCalculator metricCalculator,
            KkdMetricSnapshotService metricSnapshotService,
            DepartmentRepository departmentRepository
    ) {
        this.dataLoader = dataLoader;
        this.ruleEngine = ruleEngine;
        this.ruleRepository = ruleRepository;
        this.runRepository = runRepository;
        this.issueRepository = issueRepository;
        this.previousIssuesLoader = previousIssuesLoader;
        this.deltaCalculator = deltaCalculator;
        this.metricCalculator = metricCalculator;
        this.metricSnapshotService = metricSnapshotService;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public KkdRunResult startRun(StartKkdRunRequest request) {
        validateRequest(request);

        KkdRun previousRun = resolvePreviousRun(request.comparedToRunCode());
        KkdRun run = createRun(request, previousRun);

        KkdContext context = dataLoader.loadContext();
        Map<String, KkdRule> activeRules = loadAndValidateRules();

        List<DetectedIssue> detectedIssues = ruleEngine.evaluate(context, activeRules.keySet());

        IssueDelta delta = calculateDelta(detectedIssues, previousRun);

        saveMetricSnapshots(run, context, detectedIssues);
        saveIssues(run, detectedIssues, activeRules);

        run.complete(OffsetDateTime.now());
        runRepository.save(run);

        return buildResult(run, detectedIssues.size(), delta);
    }

    private KkdRun resolvePreviousRun(String comparedToRunCode) {
        if (comparedToRunCode == null || comparedToRunCode.isBlank()) {
            return null;
        }
        KkdRun previousRun = runRepository.findByRunCode(comparedToRunCode)
                .orElseThrow(() -> new IllegalArgumentException("Не найден предыдущий прогон: " + comparedToRunCode));

        if (!"COMPLETED".equals(previousRun.getStatus())) {
            throw new IllegalArgumentException("Сравнивать можно только завершённый прогон");
        }
        return previousRun;
    }

    private KkdRun createRun(StartKkdRunRequest request, KkdRun previousRun) {
        KkdRun run = runRepository.save(
                KkdRun.start(request.runCode(), request.snapshotDate(), OffsetDateTime.now())
        );
        if (previousRun != null) {
            run.setComparedToRunId(previousRun.getId());
        }
        return run;
    }

    private Map<String, KkdRule> loadAndValidateRules() {
        Map<String, KkdRule> rulesByCode = ruleRepository.findAllByActiveTrue().stream()
                .collect(Collectors.toMap(KkdRule::getCode, Function.identity()));

        Set<String> implementedCodes = ruleEngine.getRuleCodes();
        Set<String> unknownCodes = rulesByCode.keySet().stream()
                .filter(code -> !implementedCodes.contains(code))
                .collect(Collectors.toSet());

        if (!unknownCodes.isEmpty()) {
            throw new IllegalStateException("В БД включены правила без реализации: " + unknownCodes);
        }
        return rulesByCode;
    }

    private IssueDelta calculateDelta(List<DetectedIssue> detectedIssues, KkdRun previousRun) {
        if (previousRun == null) {
            return null;
        }
        Set<IssueKey> previousKeys = previousIssuesLoader.load(previousRun.getId());
        Set<IssueKey> currentKeys = deltaCalculator.toKeys(detectedIssues);
        return deltaCalculator.calculate(previousKeys, currentKeys);
    }

    private void saveMetricSnapshots(KkdRun run, KkdContext context, List<DetectedIssue> issues) {
        Map<Long, Long> issueCountsByGoalId = issues.stream()
                .filter(issue -> issue.objectType() == ObjectType.GOAL)
                .collect(Collectors.groupingBy(
                        DetectedIssue::objectId,
                        Collectors.counting()
                ));

        List<MetricSnapshotDto> snapshots = metricCalculator.calculate(
                context,
                departmentRepository.findAllByOrderByIdAsc(),
                issueCountsByGoalId
        );

        metricSnapshotService.save(run.getId(), snapshots);
    }

    private void saveIssues(KkdRun run, List<DetectedIssue> issues, Map<String, KkdRule> rulesByCode) {
        OffsetDateTime now = OffsetDateTime.now();
        List<KkdIssue> entities = issues.stream()
                .map(issue -> toEntity(run.getId(), issue, rulesByCode.get(issue.ruleCode()), now))
                .toList();
        issueRepository.saveAll(entities);
    }

    private KkdIssue toEntity(Long runId, DetectedIssue issue, KkdRule rule, OffsetDateTime createdAt) {
        if (rule == null) {
            throw new IllegalStateException("Не найдена запись правила: " + issue.ruleCode());
        }

        boolean isGoal = issue.objectType() == ObjectType.GOAL;

        return KkdIssue.create(
                runId,
                rule.getId(),
                isGoal ? issue.objectId() : null,
                !isGoal ? issue.objectId() : null,
                rule.getSeverity(),
                issue.description(),
                createdAt
        );
    }

    private KkdRunResult buildResult(KkdRun run, int issuesCount, IssueDelta delta) {
        return new KkdRunResult(
                run.getId(),
                run.getRunCode(),
                run.getStatus(),
                issuesCount,
                delta == null ? null : delta.newIssues(),
                delta == null ? null : delta.persistingIssues(),
                delta == null ? null : delta.resolvedIssues()
        );
    }

    private void validateRequest(StartKkdRunRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Параметры запуска обязательны");
        }
        if (request.runCode() == null || request.runCode().isBlank()) {
            throw new IllegalArgumentException("runCode не должен быть пустым");
        }
        if (request.snapshotDate() == null) {
            throw new IllegalArgumentException("snapshotDate обязателен");
        }
    }
}