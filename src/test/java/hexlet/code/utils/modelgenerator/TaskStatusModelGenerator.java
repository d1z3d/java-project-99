package hexlet.code.utils.modelgenerator;

import hexlet.code.dto.taskstatus.TaskStatusUpdateDTO;
import hexlet.code.model.TaskStatus;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TaskStatusModelGenerator {
    private Model<TaskStatus> taskStatusModel;
    private Model<TaskStatusUpdateDTO> taskStatusUpdateDTOModel;
    @Autowired
    private Faker faker;

    @PostConstruct
    public void init() {
        taskStatusModel = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .ignore(Select.field(TaskStatus::getCreatedAt))
                .supply(Select.field(TaskStatus::getName), () -> faker.name().title())
                .supply(Select.field(TaskStatus::getSlug), () -> faker.internet().slug())
                .toModel();
        taskStatusUpdateDTOModel = Instancio.of(TaskStatusUpdateDTO.class)
                .supply(Select.field(TaskStatusUpdateDTO::getName), () -> JsonNullable.of(faker.name().title()))
                .supply(Select.field(TaskStatusUpdateDTO::getSlug), () -> JsonNullable.of(faker.internet().slug()))
                .toModel();
    }
}
