package com.example.dqc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
public class KkdRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true)
    private String code;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String severity;

    @Column(name = "is_active")
    private boolean active;

    public KkdRule(){}

    public String getCode(){
        return code;
    }

    public Long getId(){
        return id;
    }

    public String getSeverity(){
        return severity;
    }

    public boolean isActive(){
        return active;
    }
}
