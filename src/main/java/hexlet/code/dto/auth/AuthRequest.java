package hexlet.code.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @Schema(example = "hexlet@example.com")
    @Email
    private String username;

    @Schema(example = "qwerty")
    @NotBlank
    @Size(min = 3)
    private String password;
}
