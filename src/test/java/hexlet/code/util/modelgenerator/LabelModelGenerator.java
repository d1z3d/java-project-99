package hexlet.code.util.modelgenerator;

import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class LabelModelGenerator {
    private Model<Label> labelModel;
    @Autowired
    private Faker faker;
    @Autowired
    private LabelRepository labelRepository;

    @PostConstruct
    public void init() {
        labelModel = Instancio.of(Label.class)
                .ignore(Select.field(Label::getId))
                .ignore(Select.field(Label::getCreatedAt))
                .supply(Select.field(Label::getName), () -> faker.lorem().characters(3, 1000))
                .toModel();
    }
}
