package hexlet.code.dto.taskstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusCreateUpdateDTO {
    private String name;
    private String slug;
}
