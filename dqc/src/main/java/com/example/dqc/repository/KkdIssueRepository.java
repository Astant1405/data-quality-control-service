package com.example.dqc.repository;

import com.example.dqc.model.KkdIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KkdIssueRepository extends JpaRepository<KkdIssue, Long> {
    List<KkdIssue> findAllByRunId(Long runId);

    @Query(value = """
        SELECT run_id, COUNT(*)
        FROM kkd_issue
        GROUP BY run_id
        """, nativeQuery = true)
    List<Object[]> countIssuesGroupedByRun();

    @Query(value = """
            WITH RECURSIVE unit_tree AS (
                SELECT id FROM department WHERE id = :unitId
                UNION ALL
                SELECT d.id FROM department d JOIN unit_tree ut ON d.parent_id = ut.id
            )
            SELECT i.* FROM kkd_issue i
            LEFT JOIN goal g ON i.goal_id = g.id
            LEFT JOIN key_result kr ON i.key_result_id = kr.id
            LEFT JOIN goal g2 ON kr.goal_id = g2.id
            LEFT JOIN kkd_rule r ON i.rule_id = r.id
            WHERE i.run_id = :runId
              AND (:unitId IS NULL OR COALESCE(g.department_id, g2.department_id) IN (SELECT id FROM unit_tree))
              AND (:ruleCode IS NULL OR r.code = :ruleCode)
              AND (:severity IS NULL OR i.severity = :severity)
            ORDER BY i.id ASC
        """,
                countQuery = """
            WITH RECURSIVE unit_tree AS (
                SELECT id FROM department WHERE id = :unitId
                UNION ALL
                SELECT d.id FROM department d JOIN unit_tree ut ON d.parent_id = ut.id
            )
            SELECT count(*) FROM kkd_issue i
            LEFT JOIN goal g ON i.goal_id = g.id
            LEFT JOIN key_result kr ON i.key_result_id = kr.id
            LEFT JOIN goal g2 ON kr.goal_id = g2.id
            LEFT JOIN kkd_rule r ON i.rule_id = r.id
            WHERE i.run_id = :runId
              AND (:unitId IS NULL OR COALESCE(g.department_id, g2.department_id) IN (SELECT id FROM unit_tree))
              AND (:ruleCode IS NULL OR r.code = :ruleCode)
              AND (:severity IS NULL OR i.severity = :severity)
        """, nativeQuery = true)
    Page<KkdIssue> findIssuesPaginated(
            @Param("runId") Long runId,
            @Param("unitId") Long unitId,
            @Param("ruleCode") String ruleCode,
            @Param("severity") String severity,
            Pageable pageable
    );

    @Query(value = """
            SELECT i.* FROM kkd_issue i
            LEFT JOIN goal g ON i.goal_id = g.id
            LEFT JOIN key_result kr ON i.key_result_id = kr.id
            LEFT JOIN goal g2 ON kr.goal_id = g2.id
            WHERE i.run_id = :runId
              AND COALESCE(g.department_id, g2.department_id) IN (:unitIds)
        """, nativeQuery = true)
    List<KkdIssue> findIssuesByRunIdAndUnitIds(@Param("runId") Long runId, @Param("unitIds") List<Long> unitIds);
}
