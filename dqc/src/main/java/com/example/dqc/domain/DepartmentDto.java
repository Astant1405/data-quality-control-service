package com.example.dqc.domain;

public record DepartmentDto(
        Long id,
        Long parentId
) {
    public boolean isRoot(){
        return parentId == null;
    }
}
