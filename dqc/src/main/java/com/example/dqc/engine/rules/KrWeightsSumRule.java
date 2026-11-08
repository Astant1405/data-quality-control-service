package com.example.dqc.engine.rules;

import com.example.dqc.engine.KkdRule;
import com.example.dqc.dto.DetectedIssue;
import com.example.dqc.dto.KkdContext;
import com.example.dqc.dto.GoalDto;
import com.example.dqc.dto.ObjectType;
import com.example.dqc.dto.KeyResultDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

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
                .filter(goal -> {
                    BigDecimal sum = sumWeights(goal);
                    return sum.compareTo(EXPECTED_SUM) != 0;
                })
                .map(goal -> {
                    BigDecimal actualSum = sumWeights(goal);
                    return new DetectedIssue(
                            code(),
                            ObjectType.GOAL,
                            goal.id(),
                            "Сумма весов ключевых результатов равна "
                                    + actualSum
                                    + "% вместо 100%"
                    );
                })
                .toList();
    }

    private BigDecimal sumWeights(GoalDto goal) {
        return goal.keyResults().stream()
                .map(KeyResultDto::weight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
