package com.example.dqc.dto;

import java.util.Map;

public record KkdContext(
        Map<Long, GoalDto> goals,
        Map<Long, DepartmentDto> departments
) {
    public KkdContext {
        goals = Map.copyOf(goals);
        departments = Map.copyOf(departments);
    }
}
