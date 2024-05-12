package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.util.AuthenticationUtils;
import hexlet.code.util.modelgenerator.LabelModelGenerator;
import hexlet.code.util.routes.LabelRoutes;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

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
public class LabelControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private LabelModelGenerator labelModelGenerator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LabelRoutes labelRoutes;
    @Autowired
    private AuthenticationUtils authenticationUtils;
    private Label testLabel;
    private String token;

    @BeforeEach
    public void setUp() throws Exception {
        testLabel = Instancio.of(labelModelGenerator.getLabelModel()).create();
        token = authenticationUtils.getToken("hexlet@example.com", "qwerty");
    }

    @Test
    public void testIndex() throws Exception {
        var response = mockMvc.perform(get(labelRoutes.indexPath())
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();
        var body = response.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        labelRepository.save(testLabel);
        var response = mockMvc.perform(get(labelRoutes.showPath(testLabel.getId()))
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();
        var body = response.getResponse().getContentAsString();
        assertThatJson(body).and(
                child -> child.node("name").isEqualTo(testLabel.getName())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var request = post(labelRoutes.createPath())
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLabel));
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var label = labelRepository.findByName(testLabel.getName()).get();

        assertNotNull(label);
        assertThat(label.getName()).isEqualTo(testLabel.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        labelRepository.save(testLabel);
        Map<String, String> data = new HashMap<>();
        data.put("name", "new label");

        var request = put(labelRoutes.updatePath(testLabel.getId()))
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());

        var label = labelRepository.findById(testLabel.getId()).get();
        assertThat(label.getName()).isEqualTo("new label");
    }

    @Test
    public void testDelete() throws Exception {
        labelRepository.save(testLabel);
        mockMvc.perform(delete(labelRoutes.deletePath(testLabel.getId()))
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNoContent());
        var isLabelExist = labelRepository.existsById(testLabel.getId());
        assertThat(isLabelExist).isFalse();
    }
}
