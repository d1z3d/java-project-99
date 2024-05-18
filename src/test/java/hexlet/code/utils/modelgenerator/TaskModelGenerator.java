package hexlet.code.utils.modelgenerator;

import hexlet.code.dto.task.TaskCreateUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Getter
public class TaskModelGenerator {
    private Model<Task> taskModel;
    private Model<TaskCreateUpdateDTO> taskCreateUpdateDTOModel;
    @Autowired
    private Faker faker;
    @Autowired
    private TaskStatusModelGenerator taskStatusModelGenerator;
    @Autowired
    private UserModelGenerator userModelGenerator;
    @Autowired
    private LabelModelGenerator labelModelGenerator;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        var taskStatus = Instancio.of(taskStatusModelGenerator.getTaskStatusModel()).create();
        var labels = labelRepository.findAll().stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
        var newLabel = Instancio.of(labelModelGenerator.getLabelModel()).create();
        var user = Instancio.of(userModelGenerator.getUserModel()).create();
        userRepository.save(user);
        taskStatusRepository.save(taskStatus);
        labelRepository.save(newLabel);

        taskModel = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .ignore(Select.field(Task::getAssignee))
                .ignore(Select.field(Task::getLabels))
                .ignore(Select.field(Task::getCreatedAt))
                .supply(Select.field(Task::getName), () -> faker.name().title())
                .supply(Select.field(Task::getIndex), () -> faker.number().positive())
                .supply(Select.field(Task::getTaskStatus), () -> taskStatus)
                .toModel();
        taskCreateUpdateDTOModel = Instancio.of(TaskCreateUpdateDTO.class)
                .ignore(Select.field(TaskCreateUpdateDTO::getAssigneeId))
                .supply(Select.field(TaskCreateUpdateDTO::getIndex), () -> faker.number().positive())
                .supply(Select.field(TaskCreateUpdateDTO::getTitle), () -> faker.name().title())
                .supply(Select.field(TaskCreateUpdateDTO::getStatus), taskStatus::getSlug)
                .toModel();
    }
}
