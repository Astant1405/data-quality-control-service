package com.example.dqc.engine.rules;

import com.example.dqc.engine.KkdRule;
import com.example.dqc.domain.DetectedIssue;
import com.example.dqc.domain.KkdContext;
import com.example.dqc.domain.GoalDto;
import com.example.dqc.domain.ObjectType;
import com.example.dqc.domain.KeyResultDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class KrWeightsSumRule implements KkdRule {

    private static final BigDecimal EXPECTED_SUM =
            new BigDecimal("100.00");

    @Override
    public String code() {
        return "KR_WEIGHTS_SUM";
    }

    @Override
    public List<DetectedIssue> evaluate(KkdContext context) {
        return context.goals().values().stream()
                .filter(GoalDto::isActive)
                .filter(goal -> !goal.keyResults().isEmpty())
                .map(this::checkGoal)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<DetectedIssue> checkGoal(GoalDto goal) {
        BigDecimal sum = goal.keyResults().stream()
                .map(KeyResultDto::weight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sum.compareTo(EXPECTED_SUM) != 0) {
            return Optional.of(new DetectedIssue(
                    code(),
                    ObjectType.GOAL,
                    goal.id(),
                    "Сумма весов КР равна " + sum + "% вместо 100%"
            ));
        }
        return Optional.empty();
    }
}
