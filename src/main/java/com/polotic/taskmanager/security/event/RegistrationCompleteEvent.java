package com.polotic.taskmanager.security.event;

import com.polotic.taskmanager.security.user.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private UserEntity userEntity;
    private String confirmationUrl;
    public RegistrationCompleteEvent(UserEntity userEntity, String confirmationUrl) {
        super(userEntity);
        this.userEntity = userEntity;
        this.confirmationUrl = confirmationUrl;
    }
}
