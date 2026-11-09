package com.example.dqc.service.metrics;

import com.example.dqc.domain.*;
import com.example.dqc.model.Department;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.Deque;
import java.util.stream.Collectors;

@Component
public class KkdMetricCalculator {

    public List<MetricSnapshotDto> calculate(
            KkdContext context,
            List<Department> departments,
            Map<Long, Long> issueCountsByGoalId
    ) {
        Map<Long, List<Long>> childrenByParent = buildChildrenMap(departments);

        return departments.stream()
                .map(dept -> calculateForDepartment(dept, context, childrenByParent, issueCountsByGoalId))
                .toList();
    }

    private MetricSnapshotDto calculateForDepartment(
            Department rootDept,
            KkdContext context,
            Map<Long, List<Long>> childrenByParent,
            Map<Long, Long> issueCountsByGoalId
    ) {
        Set<Long> unitIds = collectDescendants(rootDept.getId(), childrenByParent);

        List<GoalDto> activeGoals = context.goals().values().stream()
                .filter(GoalDto::isActive)
                .filter(goal -> unitIds.contains(goal.departmentId()))
                .toList();

        int issuesCount = 0;
        int goalsWithIssuesCount = 0;

        for (GoalDto goal : activeGoals) {
            Long count = issueCountsByGoalId.get(goal.id());
            if (count != null && count > 0) {
                goalsWithIssuesCount++;
                issuesCount += count;
            }
        }

        int activeGoalsCount = activeGoals.size();
        int cleanGoalsCount = activeGoalsCount - goalsWithIssuesCount;

        return new MetricSnapshotDto(
                rootDept.getId(),
                rootDept.getName(),
                activeGoalsCount,
                cleanGoalsCount,
                (int) goalsWithIssuesCount,
                issuesCount
        );
    }

    private Map<Long, List<Long>> buildChildrenMap(List<Department> departments) {
        return departments.stream()
                .filter(d -> d.getParentId() != null)
                .collect(Collectors.groupingBy(
                        Department::getParentId,
                        Collectors.mapping(Department::getId, Collectors.toList())
                ));
    }

    private Set<Long> collectDescendants(Long rootId, Map<Long, List<Long>> childrenByParent) {
        Set<Long> result = new LinkedHashSet<>();
        Deque<Long> queue = new ArrayDeque<>();
        queue.add(rootId);

        while (!queue.isEmpty()) {
            Long current = queue.poll();
            if (result.add(current)) {
                queue.addAll(childrenByParent.getOrDefault(current, List.of()));
            }
        }
        return result;
    }
}