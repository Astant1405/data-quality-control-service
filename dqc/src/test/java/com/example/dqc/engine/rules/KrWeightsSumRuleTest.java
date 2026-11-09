package com.example.dqc.engine.rules;

import com.example.dqc.domain.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KrWeightsSumRuleTest {

    private final KrWeightsSumRule rule = new KrWeightsSumRule();

    @Test
    void shouldFindIssueWhenWeightsSumIsNot100() {
        KeyResultDto kr1 = new KeyResultDto(1L, new BigDecimal("50.00"));
        KeyResultDto kr2 = new KeyResultDto(2L, new BigDecimal("49.99"));
        GoalDto goal = new GoalDto(1L, 1L, null, "owner", GoalStatus.ACTIVE, List.of(kr1, kr2));
        KkdContext context = new KkdContext(Map.of(1L, goal), Map.of());

        List<DetectedIssue> issues = rule.evaluate(context);

        assertEquals(1, issues.size());
        assertEquals("KR_WEIGHTS_SUM", issues.get(0).ruleCode());
    }

    @Test
    void shouldNotFindIssueWhenWeightsSumIsExactly100() {
        KeyResultDto kr1 = new KeyResultDto(1L, new BigDecimal("50.00"));
        KeyResultDto kr2 = new KeyResultDto(2L, new BigDecimal("50.00"));
        GoalDto goal = new GoalDto(1L, 1L, null, "owner", GoalStatus.ACTIVE, List.of(kr1, kr2));
        KkdContext context = new KkdContext(Map.of(1L, goal), Map.of());

        assertTrue(rule.evaluate(context).isEmpty());
    }
}