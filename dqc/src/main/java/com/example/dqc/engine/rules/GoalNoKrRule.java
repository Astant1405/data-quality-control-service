package com.example.dqc.engine.rules;

import com.example.dqc.engine.KkdRule;
import com.example.dqc.domain.DetectedIssue;
import com.example.dqc.domain.GoalDto;
import com.example.dqc.domain.KkdContext;
import com.example.dqc.domain.ObjectType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoalNoKrRule implements KkdRule {

    @Override
    public String code() {
        return "GOAL_NO_KR";
    }

    @Override
    public List<DetectedIssue> evaluate(KkdContext context) {
        return context.goals().values().stream()
                .filter(GoalDto::isActive)
                .filter(goal -> goal.keyResults().isEmpty())
                .map(goal -> new DetectedIssue(
                        code(),
                        ObjectType.GOAL,
                        goal.id(),
                        "У активной цели нет ни одного ключевого результата"
                ))
                .toList();
    }
}
