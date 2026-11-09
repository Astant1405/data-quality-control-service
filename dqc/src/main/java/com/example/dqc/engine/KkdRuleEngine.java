package com.example.dqc.engine;

import com.example.dqc.domain.DetectedIssue;
import com.example.dqc.domain.KkdContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Component
public class KkdRuleEngine {

    private final List<KkdRule> rules;

    public KkdRuleEngine(List<KkdRule> rules) {
        this.rules = rules.stream()
                .sorted(Comparator.comparing(KkdRule::code))
                .toList();
    }

    public Set<String> getRuleCodes() {
        return rules.stream()
                .map(KkdRule::code)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    public List<DetectedIssue> evaluate(KkdContext context, Set<String> activeRuleCodes) {
        return rules.stream()
                .filter(rule -> activeRuleCodes.contains(rule.code()))
                .flatMap(rule -> rule.evaluate(context).stream())
                .sorted(
                        Comparator
                                .comparing(DetectedIssue::ruleCode)
                                .thenComparing(DetectedIssue::objectId)
                )
                .toList();
    }
}
