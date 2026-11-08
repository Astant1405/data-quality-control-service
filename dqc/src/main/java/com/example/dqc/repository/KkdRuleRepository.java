package com.example.dqc.repository;

import com.example.dqc.model.KkdRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KkdRuleRepository extends JpaRepository<KkdRule, Long> {
    List<KkdRule> findAllByActiveTrue();
}
