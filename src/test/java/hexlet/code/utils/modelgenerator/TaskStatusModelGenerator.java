package hexlet.code.utils.modelgenerator;

import hexlet.code.dto.taskstatus.TaskStatusCreateUpdateDTO;
import hexlet.code.model.TaskStatus;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TaskStatusModelGenerator {
    private Model<TaskStatus> taskStatusModel;
    private Model<TaskStatusCreateUpdateDTO> taskStatusCreateUpdateDTOModel;
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
        taskStatusCreateUpdateDTOModel = Instancio.of(TaskStatusCreateUpdateDTO.class)
                .supply(Select.field(TaskStatusCreateUpdateDTO::getName), () -> faker.name().title())
                .supply(Select.field(TaskStatusCreateUpdateDTO::getSlug), () -> faker.internet().slug())
                .toModel();
    }
}
