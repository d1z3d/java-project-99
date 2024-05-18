package hexlet.code.service;

import hexlet.code.dto.user.UserCreateUpdateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.exception.ForbiddenException;
import hexlet.code.exception.ResourceConflictException;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsManager {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserUtils userUtils;

    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::map)
                .toList();
    }

    public UserDTO getById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        return userMapper.map(user);
    }

    public UserDTO create(UserCreateUpdateDTO dto) {
        var userIsExist = userRepository.findByEmail(dto.getEmail());
        if (userIsExist.isPresent()) {
            throw new ResourceConflictException("User with email " + dto.getEmail() + " already exist");
        }
        var user = userMapper.map(dto);
        userRepository.save(user);
        return userMapper.map(user);
    }

    public UserDTO update(Long id, UserUpdateDTO dto) {
        var currentUser = userUtils.getCurrentUser();
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        if (!currentUser.equals(user)) {
            throw new ForbiddenException("You have no access to update other users");
        }
        userMapper.update(dto, user);
        userRepository.save(user);
        return userMapper.map(user);
    }

    public void delete(Long id) {
        var currentUser = userUtils.getCurrentUser();
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        if (!currentUser.equals(user)) {
            throw new ForbiddenException("You have no access to delete other users");
        }
        userRepository.deleteById(id);
    }

    @Override
    public void createUser(UserDetails userData) {
        var user = new User();
        user.setEmail(userData.getUsername());
        var hashedPassword = passwordEncoder.encode(userData.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String email) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String email) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
        return user;
    }
}
