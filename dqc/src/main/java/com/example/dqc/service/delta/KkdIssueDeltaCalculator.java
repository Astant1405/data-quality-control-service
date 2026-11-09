package com.example.dqc.service.delta;

import com.example.dqc.domain.DetectedIssue;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class KkdIssueDeltaCalculator {

    public IssueDelta calculate(Set<IssueKey> previous, Set<IssueKey> current) {
        long newIssues = current.stream()
                .filter(key -> !previous.contains(key))
                .count();

        long persistingIssues = current.stream()
                .filter(previous::contains)
                .count();

        long resolvedIssues = previous.stream()
                .filter(key -> !current.contains(key))
                .count();

        return new IssueDelta(
                Math.toIntExact(newIssues),
                Math.toIntExact(persistingIssues),
                Math.toIntExact(resolvedIssues)
        );
    }

    public Set<IssueKey> toKeys(Iterable<DetectedIssue> issues) {
        Set<IssueKey> result = new HashSet<>();

        for (DetectedIssue issue : issues) {
            result.add(new IssueKey(
                    issue.ruleCode(),
                    issue.objectType(),
                    issue.objectId()
            ));
        }

        return Set.copyOf(result);
    }
}
