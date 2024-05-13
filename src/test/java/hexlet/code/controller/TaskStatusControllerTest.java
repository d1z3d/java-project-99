package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.utils.AuthenticationUtils;
import hexlet.code.utils.modelgenerator.TaskStatusModelGenerator;
import hexlet.code.utils.routes.TaskStatusRoutes;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TaskStatusModelGenerator taskStatusModelGenerator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskStatusRoutes taskStatusRoutes;
    @Autowired
    private AuthenticationUtils authenticationUtils;

    private TaskStatus testTaskStatus;
    private String token;


    @BeforeEach
    public void setUp() throws Exception {
        testTaskStatus = Instancio.of(taskStatusModelGenerator.getTaskStatusModel()).create();
        token = authenticationUtils.getToken("hexlet@example.com", "qwerty");
    }


    @Test
    public void testIndex() throws Exception {
        var response = mockMvc.perform(get(taskStatusRoutes.indexPath())
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();
        var body = response.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var response = mockMvc.perform(get(taskStatusRoutes.showPath(testTaskStatus.getId()))
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();
        var body = response.getResponse().getContentAsString();
        assertThatJson(body).and(
                child -> child.node("name").isEqualTo(testTaskStatus.getName()),
                child -> child.node("slug").isEqualTo(testTaskStatus.getSlug())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var request = post(taskStatusRoutes.createPath())
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTaskStatus));
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        var taskStatus = taskStatusRepository.findBySlug(testTaskStatus.getSlug()).get();

        assertNotNull(taskStatus);
        assertThat(taskStatus.getName()).isEqualTo(testTaskStatus.getName());
        assertThat(taskStatus.getSlug()).isEqualTo(testTaskStatus.getSlug());
    }

    @Test
    public void testUpdate() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var taskStatusUpdateDTO = Instancio.of(taskStatusModelGenerator.getTaskStatusUpdateDTOModel()).create();

        var request = put(taskStatusRoutes.updatePath(testTaskStatus.getId()))
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskStatusUpdateDTO));
        mockMvc.perform(request)
                .andExpect(status().isOk());

        var taskStatus = taskStatusRepository.findById(testTaskStatus.getId()).get();
        assertThat(taskStatus.getName()).isEqualTo(taskStatusUpdateDTO.getName().get());
        assertThat(taskStatus.getSlug()).isEqualTo(taskStatusUpdateDTO.getSlug().get());
    }

    @Test
    public void testPartiallyUpdate() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var taskStatusUpdateDTO = Instancio.of(taskStatusModelGenerator.getTaskStatusUpdateDTOModel()).create();
        taskStatusUpdateDTO.setSlug(null);

        var request = put(taskStatusRoutes.updatePath(testTaskStatus.getId()))
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskStatusUpdateDTO));
        mockMvc.perform(request)
                .andExpect(status().isOk());

        var taskStatus = taskStatusRepository.findById(testTaskStatus.getId()).get();
        assertThat(taskStatus.getName()).isEqualTo(taskStatusUpdateDTO.getName().get());
        assertThat(taskStatus.getSlug()).isEqualTo(testTaskStatus.getSlug());
    }

    @Test
    public void testDelete() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        mockMvc.perform(delete(taskStatusRoutes.deletePath(testTaskStatus.getId()))
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNoContent());
        var isTaskStatusExist = taskStatusRepository.existsById(testTaskStatus.getId());
        assertThat(isTaskStatusExist).isFalse();
    }
}
