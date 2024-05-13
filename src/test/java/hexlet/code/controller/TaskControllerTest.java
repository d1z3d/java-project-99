package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.utils.AuthenticationUtils;
import hexlet.code.utils.modelgenerator.TaskModelGenerator;
import hexlet.code.utils.routes.TaskRoutes;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private AuthenticationUtils authenticationUtils;
    @Autowired
    private TaskModelGenerator taskModelGenerator;
    @Autowired
    private TaskRoutes taskRoutes;
    private String token;
    private Task testTask;

    @BeforeEach
    public void setUp() throws Exception {
        token = authenticationUtils.getToken("hexlet@example.com", "qwerty");
        testTask = Instancio.of(taskModelGenerator.getTaskModel()).create();
    }

    @AfterEach
    public void removeAll() {
        taskRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        var response = mockMvc.perform(get(taskRoutes.indexPath())
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();
        var body = response.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }
    @Test
    public void testShow() throws Exception {
        taskRepository.save(testTask);
        var response = mockMvc.perform(get(taskRoutes.showPath(testTask.getId()))
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();
        var body = response.getResponse().getContentAsString();
        assertThatJson(body).and(
                child -> child.node("title").isEqualTo(testTask.getName()),
                child -> child.node("index").isEqualTo(testTask.getIndex()),
                child -> child.node("content").isEqualTo(testTask.getDescription()),
                child -> child.node("status").isEqualTo(testTask.getTaskStatus().getSlug())
        );
    }
    @Test
    public void testCreate() throws Exception {
        var dto = Instancio.of(taskModelGenerator.getTaskCreateDTOModel()).create();
        var request = post(taskRoutes.createPath())
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));
        var response = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var body = response.getResponse().getContentAsString();
        assertThatJson(body).and(
                child -> child.node("index").isEqualTo(dto.getIndex()),
                child -> child.node("title").isEqualTo(dto.getTitle()),
                child -> child.node("status").isEqualTo(dto.getStatus())
        );
    }
    @Test
    public void testUpdate() throws Exception {
        taskRepository.save(testTask);
        var taskUpdateDTO = Instancio.of(taskModelGenerator.getTaskUpdateDTOModel()).create();

        var request = put(taskRoutes.updatePath(testTask.getId()))
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskUpdateDTO));
        mockMvc.perform(request)
                .andExpect(status().isOk());

        var taskStatus = taskRepository.findById(testTask.getId()).get();
        assertThat(taskStatus.getIndex()).isEqualTo(taskUpdateDTO.getIndex().get());
        assertThat(taskStatus.getAssignee().getId()).isEqualTo(taskUpdateDTO.getAssigneeId().get());
        assertThat(taskStatus.getName()).isEqualTo(taskUpdateDTO.getTitle().get());
        assertThat(taskStatus.getDescription()).isEqualTo(taskUpdateDTO.getContent().get());
        assertThat(taskStatus.getTaskStatus().getSlug()).isEqualTo(taskUpdateDTO.getStatus().get());
        assertThat(taskStatus.getLabels().stream()
                    .map(Label::getId)
                    .collect(Collectors.toSet()))
                .isEqualTo(taskUpdateDTO.getTaskLabelIds().get());
    }
    @Test
    public void testPartiallyUpdate() throws Exception {
        taskRepository.save(testTask);
        var taskUpdateDTO = Instancio.of(taskModelGenerator.getTaskPartiallyDTOModel()).create();

        var request = put(taskRoutes.updatePath(testTask.getId()))
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskUpdateDTO));
        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskRepository.findById(testTask.getId()).get();
        assertThat(task.getName())
                .isEqualTo(taskUpdateDTO.getTitle().get());
    }
    @Test
    public void testDelete() throws Exception {
        taskRepository.save(testTask);
        mockMvc.perform(delete(taskRoutes.deletePath(testTask.getId()))
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNoContent());
        var isTaskExist = taskRepository.existsById(testTask.getId());
        assertThat(isTaskExist).isFalse();
    }
}
