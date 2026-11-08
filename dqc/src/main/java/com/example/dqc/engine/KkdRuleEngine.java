package com.example.dqc.engine;

import com.example.dqc.dto.DetectedIssue;
import com.example.dqc.dto.KkdContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class KkdRuleEngine {

    private final List<KkdRule> rules;

    public KkdRuleEngine(List<KkdRule> rules) {
        this.rules = rules.stream()
                .sorted(Comparator.comparing(KkdRule::code))
                .toList();
    }

    public List<DetectedIssue> evaluate(KkdContext context) {
        return rules.stream()
                .flatMap(rule ->
                        rule.evaluate(context).stream()
                )
                .sorted(
                        Comparator
                                .comparing(DetectedIssue::ruleCode)
                                .thenComparing(DetectedIssue::objectId)
                )
                .toList();
    }
}
