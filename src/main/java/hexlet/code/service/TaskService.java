package hexlet.code.service;

import hexlet.code.dto.task.TaskCreateUpdateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.specification.TaskSpecification;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final TaskSpecification taskSpecification;

    public List<TaskDTO> getAll(TaskParamsDTO params) {
        var spec = taskSpecification.build(params);
        return taskRepository.findAll(spec).stream()
                .map(taskMapper::map)
                .toList();
    }

    public TaskDTO getById(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        return taskMapper.map(task);
    }

    public TaskDTO create(TaskCreateUpdateDTO dto) {
        var task = taskMapper.map(dto);
        var taskStatus = taskStatusRepository.findBySlug(dto.getStatus().get())
                .orElseThrow(() -> new ResourceNotFoundException("Task status with " + dto.getStatus() + " not found"));
        task.setTaskStatus(taskStatus);
        if (dto.getAssigneeId() != null && dto.getAssigneeId().isPresent()) {
            var user = userRepository.findById(dto.getAssigneeId().get())
                    .orElseThrow(() -> new UsernameNotFoundException("User with id " + dto.getAssigneeId()
                            + " not found"));
            task.setAssignee(user);
        }
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public TaskDTO update(Long id, TaskCreateUpdateDTO dto) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        taskMapper.update(dto, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public void delete(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        taskRepository.delete(task);
    }
}
