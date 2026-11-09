package com.example.dqc.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "parent_id")
    private Long parentId;

    public Department(){}

    public Long getId(){
        return id;
    }

    public Long getParentId(){
        return parentId;
    }

    public String getName(){
        return name;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setParentId(Long parentId){
        this.parentId = parentId;
    }
}
