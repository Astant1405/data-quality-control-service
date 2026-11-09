package com.example.dqc.api.mapper;

import com.example.dqc.api.dto.RunInfo;
import com.example.dqc.api.dto.UnitInfo;
import com.example.dqc.model.Department;
import com.example.dqc.model.KkdRun;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface KkdMapper {

    @Mapping(target = "id", source = "runCode")
    RunInfo toRunInfo(KkdRun run);

    @Mapping(target = "id", source = "code")
    UnitInfo toUnitInfo(Department department);
}
