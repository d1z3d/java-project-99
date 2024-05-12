package hexlet.code.util.modelgenerator;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
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
public class TaskModelGenerator {
    private Model<Task> taskModel;
    private Model<TaskCreateDTO> taskCreateDTOModel;
    @Autowired
    private Faker faker;
    @Autowired
    private TaskStatusModelGenerator taskStatusModelGenerator;
    @Autowired
    private LabelModelGenerator labelModelGenerator;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;

    @PostConstruct
    public void init() {
        var taskStatus = Instancio.of(taskStatusModelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(taskStatus);

        taskModel = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .ignore(Select.field(Task::getAssignee))
                .ignore(Select.field(Task::getLabels))
                .ignore(Select.field(Task::getCreatedAt))
                .supply(Select.field(Task::getName), () -> faker.name().title())
                .supply(Select.field(Task::getIndex), () -> faker.number().positive())
                .supply(Select.field(Task::getTaskStatus), () -> taskStatus)
                .toModel();
        taskCreateDTOModel = Instancio.of(TaskCreateDTO.class)
                .supply(Select.field(TaskCreateDTO::getIndex), () -> faker.number().positive())
                .supply(Select.field(TaskCreateDTO::getAssigneeId), () -> 1L)
                .supply(Select.field(TaskCreateDTO::getTitle), () -> faker.name().title())
                .supply(Select.field(TaskCreateDTO::getStatus), taskStatus::getSlug)
                .toModel();
    }
}
