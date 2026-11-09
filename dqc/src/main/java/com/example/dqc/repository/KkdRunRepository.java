package com.example.dqc.repository;

import com.example.dqc.model.KkdRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KkdRunRepository extends JpaRepository<KkdRun, Long> {
    Optional<KkdRun> findByRunCode(String runCode);
    List<KkdRun> findAllByOrderByStartedAtDesc();
}