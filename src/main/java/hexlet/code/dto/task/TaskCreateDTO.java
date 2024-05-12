package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateDTO {
    private Integer index;
    @JsonProperty("assignee_id")
    private Long assigneeId;
    @NotBlank
    private String title;
    private String content;
    @NotNull
    private String status;
    private Set<Long> taskLabelIds;

}