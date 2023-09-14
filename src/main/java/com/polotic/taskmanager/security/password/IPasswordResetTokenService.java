package com.polotic.taskmanager.security.password;


import com.polotic.taskmanager.security.user.UserEntity;

import java.util.Optional;

public interface IPasswordResetTokenService {
    String validatePasswordResetToken(String theToken);

    Optional<UserEntity> findUserByPasswordResetToken(String theToken);

    void resetPassword(UserEntity theUser, String password);

    void createPasswordResetTokenForUser(UserEntity user, String passwordResetToken);
}
