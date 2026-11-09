package com.example.dqc.repository;

import com.example.dqc.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByOrderByIdAsc();
    @Query(value = """
            WITH RECURSIVE subtree AS (
                SELECT id FROM department WHERE id = :rootId
                UNION ALL
                SELECT d.id FROM department d
                INNER JOIN subtree s ON d.parent_id = s.id
            )
            SELECT id FROM subtree
        """, nativeQuery = true)
    List<Long> findSubtreeIds(@Param("rootId") Long rootId);
}
