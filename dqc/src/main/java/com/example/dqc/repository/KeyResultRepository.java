package com.example.dqc.repository;

import com.example.dqc.model.KeyResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {
    List<KeyResult> findAllByOrderByAsc();
}
