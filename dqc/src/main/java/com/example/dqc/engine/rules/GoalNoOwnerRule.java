package com.example.dqc.engine.rules;

import com.example.dqc.engine.KkdRule;
import com.example.dqc.dto.DetectedIssue;
import com.example.dqc.dto.GoalDto;
import com.example.dqc.dto.KkdContext;
import com.example.dqc.dto.ObjectType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoalNoOwnerRule implements KkdRule {

    @Override
    public String code(){
        return "GOAL_NO_OWNER";
    }

    @Override
    public List<DetectedIssue> evaluate(KkdContext context){
        return context.goals().values().stream()
                .filter(GoalDto::isActive)
                .filter(goal -> !goal.hasOwner())
                .map(goal -> new DetectedIssue(
                        code(),
                        ObjectType.GOAL,
                        goal.id(),
                        "У цели отсутствует ответственный"
                ))
                .toList();
    }
}
