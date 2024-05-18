package hexlet.code.app.component;

import hexlet.code.dto.label.LabelModernizeDTO;
import hexlet.code.dto.taskstatus.TaskStatusCreateUpdateDTO;
import hexlet.code.dto.user.UserCreateUpdateDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;
    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusMapper taskStatusMapper;
    private final LabelMapper labelMapper;
    private final LabelRepository labelRepository;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        var userDetails = new UserCreateUpdateDTO();
        userDetails.setEmail("hexlet@example.com");
        userDetails.setPassword("qwerty");
        userService.create(userDetails);

        List<TaskStatusCreateUpdateDTO> taskStatuses = Arrays.asList(
                new TaskStatusCreateUpdateDTO("Draft", "draft"),
                new TaskStatusCreateUpdateDTO("ToReview", "to_review"),
                new TaskStatusCreateUpdateDTO("ToBeFixed", "to_be_fixed"),
                new TaskStatusCreateUpdateDTO("ToPublish", "to_publish"),
                new TaskStatusCreateUpdateDTO("Published", "published")
        );
        taskStatuses.forEach(dto -> {
            var taskStatus = taskStatusMapper.map(dto);
            taskStatusRepository.save(taskStatus);
        });

        Set<LabelModernizeDTO> labels = Set.of(
                new LabelModernizeDTO("bug"),
                new LabelModernizeDTO("feature")
        );
        labels.forEach(dto -> {
            var label = labelMapper.map(dto);
            labelRepository.save(label);
        });
    }
}
