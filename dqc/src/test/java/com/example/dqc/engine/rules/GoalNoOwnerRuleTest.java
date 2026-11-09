package com.example.dqc.engine.rules;

import com.example.dqc.domain.DetectedIssue;
import com.example.dqc.domain.GoalDto;
import com.example.dqc.domain.GoalStatus;
import com.example.dqc.domain.KkdContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GoalNoOwnerRuleTest {

    private final GoalNoOwnerRule rule = new GoalNoOwnerRule();

    @Test
    void shouldFindIssueWhenActiveGoalHasNoOwner() {
        GoalDto goal = new GoalDto(1L, 1L, null, null, GoalStatus.ACTIVE, List.of());
        KkdContext context = new KkdContext(Map.of(1L, goal), Map.of());

        List<DetectedIssue> issues = rule.evaluate(context);

        assertEquals(1, issues.size());
        assertEquals("GOAL_NO_OWNER", issues.get(0).ruleCode());
        assertEquals(1L, issues.get(0).objectId());
    }

    @Test
    void shouldNotFindIssueWhenActiveGoalHasValidOwner() {
        GoalDto goal = new GoalDto(1L, 1L, null, "EMP-123", GoalStatus.ACTIVE, List.of());
        KkdContext context = new KkdContext(Map.of(1L, goal), Map.of());
        assertTrue(rule.evaluate(context).isEmpty());
    }

    @Test
    void shouldFindIssueWhenOwnerIsBlankString() {
        GoalDto goal = new GoalDto(1L, 1L, null, "   ", GoalStatus.ACTIVE, List.of());
        KkdContext context = new KkdContext(Map.of(1L, goal), Map.of());
        assertEquals(1, rule.evaluate(context).size());
    }

    @Test
    void shouldNotFindIssueForInactiveGoal() {
        GoalDto goal = new GoalDto(1L, 1L, null, null, GoalStatus.DRAFT, List.of());
        KkdContext context = new KkdContext(Map.of(1L, goal), Map.of());
        assertTrue(rule.evaluate(context).isEmpty());
    }
}