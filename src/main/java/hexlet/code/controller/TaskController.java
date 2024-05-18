package hexlet.code.controller;

import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Task")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> index(TaskParamsDTO params) {
        var tasks = taskService.getAll(params);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(tasks.size()))
                .body(tasks);
    }

    @GetMapping("/tasks/{id}")
    public TaskDTO show(@PathVariable("id") Long id) {
        return taskService.getById(id);
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "Bearer Authentication")
    public TaskDTO create(@RequestBody TaskDTO dto) {
        return taskService.create(dto);
    }

    @PutMapping("/tasks/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public TaskDTO update(@PathVariable("id") Long id, @RequestBody TaskDTO dto) {
        return taskService.update(id, dto);
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "Bearer Authentication")
    public void delete(@PathVariable("id") Long id) {
        taskService.delete(id);
    }
}
