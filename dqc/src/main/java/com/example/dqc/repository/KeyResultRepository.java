package com.example.dqc.repository;

import com.example.dqc.model.KeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {
    List<KeyResult> findAllByOrderByIdAsc();
}
