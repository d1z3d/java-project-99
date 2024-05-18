package hexlet.code.controller;

import hexlet.code.dto.user.UserCreateUpdateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "User")
@AllArgsConstructor
public class UsersController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> index() {
        var users = userService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(users);
    }

    @GetMapping("/users/{id}")
    public UserDTO show(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateUpdateDTO dto) {
        return userService.create(dto);
    }

    @PutMapping("/users/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public UserDTO update(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateDTO dto) {
        return userService.update(id, dto);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "Bearer Authentication")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }
}
