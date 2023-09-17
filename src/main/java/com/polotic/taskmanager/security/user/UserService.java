package com.polotic.taskmanager.security.user;

import com.polotic.taskmanager.security.dto.RegistrationRequest;
import com.polotic.taskmanager.security.token.VerificationTokenService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, VerificationTokenService verificationTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenService = verificationTokenService;
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity registerUser(RegistrationRequest registration) {
        UserEntity user = new UserEntity(
                registration.getName(),
                registration.getSurname(),
                registration.getEmail(),
                passwordEncoder.encode(registration.getPassword()),
                Collections.singletonList(new RoleEntity("ROLE_USER"))
        );

        return userRepository.save(user);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public void updateUser(Long id, String name, String surname, String email) {
        userRepository.update(name, surname, email, id);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        Optional<UserEntity> theUser = userRepository.findById(id);
        theUser.ifPresent(user -> verificationTokenService.deleteUserToken(user.getId()));
        userRepository.deleteById(id);
    }
}
