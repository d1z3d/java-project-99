package hexlet.code.mapper;

import hexlet.code.dto.taskstatus.TaskStatusCreateUpdateDTO;
import hexlet.code.dto.taskstatus.TaskStatusDTO;
import hexlet.code.model.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = ReferenceMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {
    public abstract TaskStatusDTO map(TaskStatus model);
    public abstract TaskStatus map(TaskStatusCreateUpdateDTO dto);
    public abstract void update(TaskStatusCreateUpdateDTO dto, @MappingTarget TaskStatus model);
}
