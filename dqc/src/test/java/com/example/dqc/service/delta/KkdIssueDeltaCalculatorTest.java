package com.example.dqc.service.delta;

import com.example.dqc.domain.ObjectType;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KkdIssueDeltaCalculatorTest {

    private final KkdIssueDeltaCalculator calculator = new KkdIssueDeltaCalculator();

    @Test
    void shouldCalculateDeltaCorrectly() {
        IssueKey persisting = new IssueKey("GOAL_NO_OWNER", ObjectType.GOAL, 1L);
        IssueKey resolved = new IssueKey("GOAL_NO_KR", ObjectType.GOAL, 2L);
        IssueKey newIssue = new IssueKey("KR_WEIGHTS_SUM", ObjectType.GOAL, 3L);

        Set<IssueKey> previous = Set.of(persisting, resolved);
        Set<IssueKey> current = Set.of(persisting, newIssue);

        IssueDelta delta = calculator.calculate(previous, current);

        assertEquals(1, delta.newIssues());
        assertEquals(1, delta.persistingIssues());
        assertEquals(1, delta.resolvedIssues());
    }
}
