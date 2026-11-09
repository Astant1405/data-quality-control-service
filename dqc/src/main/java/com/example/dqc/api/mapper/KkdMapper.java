package com.example.dqc.api.mapper;

import com.example.dqc.api.dto.RunInfo;
import com.example.dqc.api.dto.UnitInfo;
import com.example.dqc.model.Department;
import com.example.dqc.model.KkdRun;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KkdMapper {

    RunInfo toRunInfo(KkdRun run);
    UnitInfo toUnitInfo(Department department);
}
