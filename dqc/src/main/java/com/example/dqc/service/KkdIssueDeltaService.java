package com.example.dqc.service;

import com.example.dqc.domain.ObjectType;
import com.example.dqc.model.KkdIssue;
import com.example.dqc.service.delta.IssueDelta;
import com.example.dqc.service.delta.IssueKey;
import com.example.dqc.service.delta.KkdIssueDeltaCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KkdIssueDeltaService {

    private final KkdIssueScopeService issueScopeService;
    private final KkdIssueDeltaCalculator deltaCalculator;

    public KkdIssueDeltaService(KkdIssueScopeService issueScopeService, KkdIssueDeltaCalculator deltaCalculator) {
        this.issueScopeService = issueScopeService;
        this.deltaCalculator = deltaCalculator;
    }

    @Transactional(readOnly = true)
    public DeltaResult calculate(Long currentRunId, Long previousRunId, Set<Long> unitIds) {
        List<KkdIssue> currentIssues = issueScopeService.findIssues(currentRunId, unitIds);
        List<KkdIssue> previousIssues = issueScopeService.findIssues(previousRunId, unitIds);

        Map<Long, String> ruleCodes = issueScopeService.getRuleCodes();

        Set<IssueKey> currentKeys = toKeys(currentIssues, ruleCodes);
        Set<IssueKey> previousKeys = toKeys(previousIssues, ruleCodes);

        IssueDelta delta = deltaCalculator.calculate(previousKeys, currentKeys);

        return new DeltaResult(
                delta.newIssues(),
                delta.persistingIssues(),
                delta.resolvedIssues()
        );
    }

    private Set<IssueKey> toKeys(List<KkdIssue> issues, Map<Long, String> ruleCodes) {
        return issues.stream()
                .map(issue -> {
                    boolean isGoal = issue.getGoalId() != null;
                    Long objectId = isGoal ? issue.getGoalId() : issue.getKeyResultId();

                    return new IssueKey(
                            ruleCodes.get(issue.getRuleId()),
                            isGoal ? ObjectType.GOAL : ObjectType.KEY_RESULT,
                            objectId
                    );
                })
                .collect(Collectors.toSet());
    }

    public record DeltaResult(
            Integer newIssues,
            Integer persistingIssues,
            Integer resolvedIssues
    ) {}
}