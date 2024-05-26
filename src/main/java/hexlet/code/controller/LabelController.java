package hexlet.code.controller;

import hexlet.code.dto.label.LabelCreateUpdateDto;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.service.LabelService;
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

import java.util.Set;

@RestController
@RequestMapping("/api")
@Tag(name = "Label")
@SecurityRequirement(name = "Bearer Authentication")
@AllArgsConstructor
public class LabelController {

    private final LabelService labelService;

    @GetMapping("/labels")
    public ResponseEntity<Set<LabelDTO>> index() {
        var labels = labelService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(labels);
    }

    @GetMapping("/labels/{id}")
    public LabelDTO show(@PathVariable("id") Long id) {
        return labelService.getById(id);
    }

    @PostMapping("/labels")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@Valid @RequestBody LabelCreateUpdateDto dto) {
        return labelService.create(dto);
    }

    @PutMapping("/labels/{id}")
    public LabelDTO update(@PathVariable("id") Long id, @Valid @RequestBody LabelCreateUpdateDto dto) {
        return labelService.update(id, dto);
    }

    @DeleteMapping("/labels/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        labelService.delete(id);
    }
}
