package com.polotic.taskmanager.security.user;


import com.polotic.taskmanager.security.dto.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<UserEntity> getAllUsers();

    UserEntity registerUser(RegistrationRequest registrationRequest);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(Long id);

    void updateUser(Long id, String firstName, String lastName, String email);

    void deleteUser(Long id);
}
