package hexlet.code.utils.routes;

import org.springframework.stereotype.Component;

@Component
public class TaskRoutes {
    public String indexPath() {
        return "/api/tasks";
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
