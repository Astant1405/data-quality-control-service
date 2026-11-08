INSERT INTO kkd_rules (
    code,
    description,
    severity,
    is_active
)
VALUES
(
    'GOAL_NO_OWNER',
    'Цель не имеет ответственного',
    'critical',
    TRUE
),
(
    'GOAL_NO_KR',
    'У активной цели нет ни одного ключевого результата',
    'critical',
    TRUE
),
(
    'KR_WEIGHTS_SUM',
    'Сумма весов ключевых результатов не равна 100%',
    'major',
    TRUE
),
(
    'CASCADE_NO_PARENT',
    'Цель не связана с целью вышестоящего уровня',
    'major',
    TRUE
);