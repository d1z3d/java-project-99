package hexlet.code.mapper;

import hexlet.code.dto.user.UserCreateUpdateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.model.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public abstract UserDTO map(User model);
    public abstract User map(UserCreateUpdateDTO dto);
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);

    @BeforeMapping
    public void encryptPassword(UserCreateUpdateDTO dto) {
        var password = dto.getPassword();
        dto.setPassword(passwordEncoder.encode(password));
    }
    @BeforeMapping
    public void encryptPasswordUpdate(UserUpdateDTO userUpdateDTO, @MappingTarget User model) {
        var password = userUpdateDTO.getPassword();
        if (password != null && password.isPresent()) {
            model.setPassword(passwordEncoder.encode(password.get()));
        }
    }
}
