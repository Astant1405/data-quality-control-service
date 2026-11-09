package com.example.dqc.service;

import com.example.dqc.domain.GoalDto;
import com.example.dqc.domain.GoalStatus;
import com.example.dqc.domain.KeyResultDto;
import com.example.dqc.domain.KkdContext;
import com.example.dqc.domain.DepartmentDto;
import com.example.dqc.model.Department;
import com.example.dqc.model.Goal;
import com.example.dqc.model.KeyResult;
import com.example.dqc.repository.DepartmentRepository;
import com.example.dqc.repository.GoalRepository;
import com.example.dqc.repository.KeyResultRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class KkdDataLoader {

    private final GoalRepository goalRepository;
    private final KeyResultRepository keyResultRepository;
    private final DepartmentRepository departmentRepository;

    public KkdDataLoader(GoalRepository goalRepository, KeyResultRepository keyResultRepository, DepartmentRepository departmentRepository) {
        this.goalRepository = goalRepository;
        this.keyResultRepository = keyResultRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public KkdContext loadContext() {
        List<Goal> goalEntities = goalRepository.findAll();
        List<KeyResult> krEntities = keyResultRepository.findAllByOrderByIdAsc();
        List<Department> departmentEntities = departmentRepository.findAll();

        Map<Long, List<KeyResultDto>> keyResultsByGoal =
                krEntities.stream()
                        .collect(Collectors.groupingBy(
                                KeyResult::getGoalId,
                                Collectors.mapping(
                                        kr -> new KeyResultDto(
                                                kr.getId(),
                                                kr.getWeight()
                                        ),
                                        Collectors.toList()
                                )
                        ));

        Map<Long, GoalDto> goals =
                goalEntities.stream()
                        .map(entity -> new GoalDto(
                                entity.getId(),
                                entity.getDepartmentId(),
                                entity.getParentGoalId(),
                                entity.getOwnerId(),
                                GoalStatus.valueOf(entity.getStatus()),
                                keyResultsByGoal.getOrDefault(
                                        entity.getId(),
                                        List.of()
                                )
                        ))
                        .collect(Collectors.toMap(
                                GoalDto::id,
                                Function.identity()
                        ));

        Map<Long, DepartmentDto> departments =
                departmentEntities.stream()
                        .map(entity -> new DepartmentDto(
                                entity.getId(),
                                entity.getParentId()
                        ))
                        .collect(Collectors.toMap(
                                DepartmentDto::id,
                                Function.identity()
                        ));

        return new KkdContext(goals, departments);
    }
}
