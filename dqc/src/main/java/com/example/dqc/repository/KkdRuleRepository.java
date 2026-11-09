package com.example.dqc.repository;

import com.example.dqc.model.KkdRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KkdRuleRepository extends JpaRepository<KkdRule, Long> {
    List<KkdRule> findAllByActiveTrue();
}
