package com.polotic.taskmanager.security.token;

import com.polotic.taskmanager.security.user.UserEntity;

import java.util.Optional;

public interface IVerificationTokenService {
    String validateToken(String token);

    void saveVerificationTokenForUser(UserEntity user, String token);

    Optional<VerificationToken> findByToken(String token);

    void deleteUserToken(Long id);
}
