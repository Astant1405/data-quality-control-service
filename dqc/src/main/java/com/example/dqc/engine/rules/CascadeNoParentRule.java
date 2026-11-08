package com.example.dqc.engine.rules;

import com.example.dqc.engine.KkdRule;
import com.example.dqc.dto.GoalDto;
import com.example.dqc.dto.KkdContext;
import com.example.dqc.dto.DetectedIssue;
import com.example.dqc.dto.ObjectType;
import com.example.dqc.dto.DepartmentDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CascadeNoParentRule implements KkdRule {

    @Override
    public String code() {
        return "CASCADE_NO_PARENT";
    }

    @Override
    public List<DetectedIssue> evaluate(KkdContext context) {
        return context.goals().values().stream()
                .filter(GoalDto::isActive)
                .filter(goal -> hasCascadeProblem(goal, context))
                .map(goal -> new DetectedIssue(
                        code(),
                        ObjectType.GOAL,
                        goal.id(),
                        "Цель не связана с целью непосредственного "
                                + "вышестоящего подразделения"
                ))
                .toList();
    }

    private boolean hasCascadeProblem(GoalDto goal, KkdContext context) {
        DepartmentDto department = context.departments().get(goal.departmentId());

        if (department == null || department.isRoot()) {
            return false;
        }

        if (goal.parentGoalId() == null) {
            return true;
        }

        GoalDto parentGoal = context.goals().get(goal.parentGoalId());

        return parentGoal == null || !department.parentId().equals(parentGoal.departmentId());
    }
}
