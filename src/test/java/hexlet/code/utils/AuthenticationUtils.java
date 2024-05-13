package hexlet.code.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.auth.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class AuthenticationUtils {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    public String getToken(String username, String password) throws Exception {
        var user = objectMapper.writeValueAsString(
                new AuthRequest(username, password)
        );
        var response = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(user))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return withPrefix(response);
    }

    private String withPrefix(String data) {
        return "Bearer " + data;
    }
}
