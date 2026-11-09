package com.example.dqc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import java.time.LocalDate;

@Entity
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_id")
    private Long departmentId;

    private String ownerId;

    @Column(name = "parent_goal_id")
    private Long parentGoalId;

    @Column(nullable = false)
    private String title;
    private String description;

    @Column(nullable = false)
    private String status;

    private LocalDate dueDate;

    public Goal(){}

    public Long getId(){
        return id;
    }

    public Long getDepartmentId(){
        return departmentId;
    }

    public Long getParentGoalId(){
        return parentGoalId;
    }

    public String getOwnerId(){
        return  ownerId;
    }

    public String getStatus(){
        return status;
    }
}
