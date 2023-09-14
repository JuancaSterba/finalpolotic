package com.polotic.taskmanager.security.dto;

import com.polotic.taskmanager.security.user.RoleEntity;
import lombok.Data;

import java.util.List;

@Data
public class RegistrationRequest {
    private String name;
    private String surname;
    private String email;
    private String password;
    private List<RoleEntity> roles;
}
