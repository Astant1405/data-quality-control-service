package com.example.dqc.repository;

import com.example.dqc.model.KkdRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KkdRunRepository extends JpaRepository<KkdRun, Long> {
    boolean existsByRunCode(String runCode);
}