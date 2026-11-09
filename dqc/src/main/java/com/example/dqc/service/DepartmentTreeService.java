package com.example.dqc.service;

import com.example.dqc.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DepartmentTreeService {

    private final DepartmentRepository departmentRepository;

    public DepartmentTreeService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Set<Long> getSubtreeIds(Long rootId) {
        return new HashSet<>(departmentRepository.findSubtreeIds(rootId));
    }
}
