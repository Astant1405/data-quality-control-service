package com.example.dqc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import java.math.BigDecimal;

@Entity
public class KeyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long goalId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal weight;

    public KeyResult(){}

    public Long getGoalId(){
        return  goalId;
    }

    public Long getId(){
        return id;
    }

    public BigDecimal getWeight(){
        return weight;
    }
}
