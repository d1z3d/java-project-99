package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import hexlet.code.utils.AuthenticationUtils;
import hexlet.code.utils.modelgenerator.UserModelGenerator;
import hexlet.code.utils.routes.UserRoutes;
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
public class UsersControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserModelGenerator userModelGenerator;
    @Autowired
    private AuthenticationUtils authenticationUtils;
    @Autowired
    private UserRoutes userRoutes;
    private String tokenAdmin;
    private String tokenTestUser;
    private User testUser;

    @BeforeEach
    public void setUp() throws Exception {
        testUser = Instancio.of(userModelGenerator.getUserModel())
                .create();
        userService.createUser(testUser);
        var id = userRepository.findByEmail(testUser.getEmail()).get().getId();
        testUser.setId(id);
        tokenAdmin = authenticationUtils.getToken("hexlet@example.com", "qwerty");
        tokenTestUser = authenticationUtils.getToken(testUser.getUsername(), testUser.getPassword());
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get(userRoutes.indexPath())
                        .header(HttpHeaders.AUTHORIZATION, tokenTestUser))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        var result = mockMvc.perform(get(userRoutes.showPath(testUser.getId()))
                        .header(HttpHeaders.AUTHORIZATION, tokenAdmin))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                child -> child.node("email").isEqualTo(testUser.getEmail())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var data = Instancio.of(userModelGenerator.getUserModel())
                .create();

        var request = post(userRoutes.createPath())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(data.getEmail()).get();

        assertNotNull(user);
        assertThat(user.getFirstName()).isEqualTo(data.getFirstName());
        assertThat(user.getLastName()).isEqualTo(data.getLastName());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new HashMap<>();
        data.put("firstName", "Andrey");
        data.put("lastName", "Borychev");
        data.put("email", "borychev.au@gmail.com");

        var request = put(userRoutes.updatePath(testUser.getId()))
                    .header(HttpHeaders.AUTHORIZATION, tokenTestUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var user = userRepository.findById(testUser.getId()).get();
        assertThat(user.getFirstName()).isEqualTo("Andrey");
        assertThat(user.getLastName()).isEqualTo("Borychev");
        assertThat(user.getEmail()).isEqualTo("borychev.au@gmail.com");

        var request2 = put(userRoutes.updatePath(testUser.getId()))
                    .header(HttpHeaders.AUTHORIZATION, tokenAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));
        var result = mockMvc.perform(request2)
                .andExpect(status().isForbidden())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThat(body).isEqualTo("You have no access to update other users");
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(userRoutes.deletePath(testUser.getId()))
                        .header(HttpHeaders.AUTHORIZATION, tokenTestUser))
                .andExpect(status().isNoContent());
        var isUserExist = userRepository.existsById(testUser.getId());
        assertThat(isUserExist).isFalse();
    }
}
