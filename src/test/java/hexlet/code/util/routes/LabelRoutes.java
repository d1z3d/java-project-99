package hexlet.code.util.routes;

import org.springframework.stereotype.Component;

@Component
public class LabelRoutes {
    public String indexPath() {
        return "/api/labels";
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
