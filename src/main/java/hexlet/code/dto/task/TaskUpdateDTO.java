package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateDTO {
    private JsonNullable<Integer> index;
    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;
    private JsonNullable<String> title;
    private JsonNullable<String> content;
    private JsonNullable<String> status;
    private JsonNullable<Set<Long>> taskLabelIds;
}
