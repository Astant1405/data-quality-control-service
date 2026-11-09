package com.example.dqc.service.metrics;

import com.example.dqc.domain.DepartmentDto;
import com.example.dqc.domain.GoalDto;
import com.example.dqc.domain.GoalStatus;
import com.example.dqc.domain.KkdContext;
import com.example.dqc.domain.MetricSnapshotDto;
import com.example.dqc.model.Department;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KkdMetricCalculatorTest {

    private final KkdMetricCalculator calculator = new KkdMetricCalculator();

    @Test
    void shouldCalculateMetricsForDepartmentWithChildren() {
        Department root = new Department();
        root.setId(1L);
        root.setName("Головной офис");

        Department child = new Department();
        child.setId(2L);
        child.setName("Дочерний");
        child.setParentId(1L);

        GoalDto goal = new GoalDto(10L, 2L, null, "owner", GoalStatus.ACTIVE, List.of());

        KkdContext context = new KkdContext(
                Map.of(10L, goal),
                Map.of(1L, new DepartmentDto(1L, null), 2L, new DepartmentDto(2L, 1L))
        );

        Map<Long, Long> issuesCountByGoalId = Map.of(10L, 1L);

        List<MetricSnapshotDto> result = calculator.calculate(
                context,
                List.of(root, child),
                issuesCountByGoalId
        );

        MetricSnapshotDto rootMetrics = result.stream().filter(m -> m.unitId().equals(1L)).findFirst().orElseThrow();

        assertEquals(1, rootMetrics.activeGoalsCount());
        assertEquals(0, rootMetrics.cleanGoalsCount());
        assertEquals(1, rootMetrics.goalsWithIssuesCount());
        assertEquals(1, rootMetrics.issuesCount());
    }
}