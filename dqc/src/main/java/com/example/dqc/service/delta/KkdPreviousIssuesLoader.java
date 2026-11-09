package com.example.dqc.service.delta;

import com.example.dqc.domain.ObjectType;
import com.example.dqc.model.KkdRule;
import com.example.dqc.repository.KkdIssueRepository;
import com.example.dqc.repository.KkdRuleRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class KkdPreviousIssuesLoader {

    private final KkdIssueRepository issueRepository;
    private final KkdRuleRepository ruleRepository;

    public KkdPreviousIssuesLoader(KkdIssueRepository issueRepository, KkdRuleRepository ruleRepository) {
        this.issueRepository = issueRepository;
        this.ruleRepository = ruleRepository;
    }

    public Set<IssueKey> load(Long runId) {
        var issues = issueRepository.findAllByRunId(runId);

        Map<Long, KkdRule> rulesById =
                ruleRepository.findAll().stream()
                        .collect(Collectors.toMap(
                                KkdRule::getId,
                                Function.identity()
                        ));

        return issues.stream()
                .map(issue -> {
                    KkdRule rule = rulesById.get(issue.getRuleId());

                    if (rule == null) {
                        throw new IllegalStateException(
                                "Не найдено правило с ID "
                                        + issue.getRuleId()
                        );
                    }

                    ObjectType type = issue.getGoalId() != null
                            ? ObjectType.GOAL
                            : ObjectType.KEY_RESULT;

                    Long objectId = issue.getGoalId() != null
                            ? issue.getGoalId()
                            : issue.getKeyResultId();

                    return new IssueKey(
                            rule.getCode(),
                            type,
                            objectId
                    );
                })
                .collect(Collectors.toUnmodifiableSet());
    }
}
