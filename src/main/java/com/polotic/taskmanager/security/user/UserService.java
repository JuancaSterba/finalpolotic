package com.polotic.taskmanager.security.user;

import com.polotic.taskmanager.security.dto.RegistrationRequest;
import com.polotic.taskmanager.security.token.VerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity registerUser(RegistrationRequest registration) {
        var user = new UserEntity(
                registration.getName(),
                registration.getSurname(),
                registration.getEmail(),
                passwordEncoder.encode(registration.getPassword()),
                Arrays.asList(new RoleEntity("ROLE_USER"))
        );
        return userRepository.save(user);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        System.out.println(email + "-------------------");
        return Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public void updateUser(Long id, String name, String surame, String email) {
        userRepository.update(name, surame, email, id);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        Optional<UserEntity> theUser = userRepository.findById(id);
        theUser.ifPresent(user -> verificationTokenService.deleteUserToken(user.getId()));
        userRepository.deleteById(id);
    }
}
