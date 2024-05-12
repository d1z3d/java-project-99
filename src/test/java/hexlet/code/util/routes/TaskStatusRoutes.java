package hexlet.code.util.routes;

import org.springframework.stereotype.Component;

@Component
public class TaskStatusRoutes {
    public String indexPath() {
        return "/api/task_statuses";
    }
    public String showPath(Long id) {
        return indexPath() + "/" + id;
    }
    public String createPath() {
        return indexPath();
    }
    public String updatePath(Long id) {
        return showPath(id);
    }
    public String deletePath(Long id) {
        return showPath(id);
    }
}
