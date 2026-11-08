package com.example.dqc.dto;

public record DepartmentDto(
        Long id,
        Long parentId
) {
    public boolean isRoot(){
        return parentId == null;
    }
}
