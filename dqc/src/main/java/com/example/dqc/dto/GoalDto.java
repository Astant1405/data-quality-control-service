package com.example.dqc.dto;

import java.util.List;

public record GoalDto(
        Long id,
        Long departmentId,
        Long parentGoalId,
        String ownerId,
        GoalStatus status,
        List<KeyResultDto> keyResults
) {
    public GoalDto {
        keyResults = keyResults == null
                ? List.of()
                : List.copyOf(keyResults);
    }

    public boolean isActive() {
        return status == GoalStatus.ACTIVE;
    }

    public boolean hasOwner() {
        return ownerId != null && !ownerId.isBlank();
    }
}
