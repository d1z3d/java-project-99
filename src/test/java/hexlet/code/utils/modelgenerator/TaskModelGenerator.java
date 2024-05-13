package hexlet.code.utils.modelgenerator;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
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
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Getter
public class TaskModelGenerator {
    private Model<Task> taskModel;
    private Model<TaskCreateDTO> taskCreateDTOModel;
    private Model<TaskUpdateDTO> taskUpdateDTOModel;
    private Model<TaskUpdateDTO> taskPartiallyDTOModel;
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
        taskCreateDTOModel = Instancio.of(TaskCreateDTO.class)
                .ignore(Select.field(TaskCreateDTO::getAssigneeId))
                .supply(Select.field(TaskCreateDTO::getIndex), () -> faker.number().positive())
                .supply(Select.field(TaskCreateDTO::getTitle), () -> faker.name().title())
                .supply(Select.field(TaskCreateDTO::getStatus), taskStatus::getSlug)
                .toModel();
        taskUpdateDTOModel = Instancio.of(TaskUpdateDTO.class)
                .supply(Select.field(TaskUpdateDTO::getIndex), () -> JsonNullable.of(faker.number().positive()))
                .supply(Select.field(TaskUpdateDTO::getAssigneeId), () -> JsonNullable.of(user.getId()))
                .supply(Select.field(TaskUpdateDTO::getTitle), () -> JsonNullable.of(faker.name().title()))
                .supply(Select.field(TaskUpdateDTO::getContent),
                        () -> JsonNullable.of(faker.lorem().characters(1, 255)))
                .supply(Select.field(TaskUpdateDTO::getStatus), () -> JsonNullable.of(taskStatus.getSlug()))
                .supply(Select.field(TaskUpdateDTO::getTaskLabelIds), () -> JsonNullable.of(labels))
                .toModel();
        taskPartiallyDTOModel = Instancio.of(TaskUpdateDTO.class)
                .ignore(Select.field(TaskUpdateDTO::getIndex))
                .ignore(Select.field(TaskUpdateDTO::getAssigneeId))
                .ignore(Select.field(TaskUpdateDTO::getContent))
                .ignore(Select.field(TaskUpdateDTO::getStatus))
                .ignore(Select.field(TaskUpdateDTO::getTaskLabelIds))
                .supply(Select.field(TaskUpdateDTO::getTitle), () -> JsonNullable.of(faker.name().title()))
                .toModel();
    }
}
