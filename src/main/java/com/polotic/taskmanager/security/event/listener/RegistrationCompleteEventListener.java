package com.polotic.taskmanager.security.event.listener;

import com.polotic.taskmanager.security.event.RegistrationCompleteEvent;
import com.polotic.taskmanager.security.token.VerificationTokenService;
import com.polotic.taskmanager.security.user.UserEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final VerificationTokenService tokenService;
    private final JavaMailSender mailSender;
    private UserEntity userEntity;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //1. get the user
        userEntity = event.getUserEntity();
        //2. generate a token for the user
        String vToken = UUID.randomUUID().toString();
        //3. save the token for the user
        tokenService.saveVerificationTokenForUser(userEntity, vToken);
        //4. Build the verification url
        String url = event.getConfirmationUrl() + "/registration/verifyEmail?token=" + vToken;
        //5. send the email to the user
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Verificación de correo electrónico";
        String senderName = "Servicio de Verificación de Usuarios";
        String mailContent = "<p> ¡Hola, " + userEntity.getName() + "!</p>" +
                "<p>Gracias por registrarte con nosotros. Por favor, sigue el enlace a continuación para completar tu registro.</p>" +
                "<a href=\"" + url + "\">Verifica tu correo electrónico para activar tu cuenta</a>" +
                "<p>Gracias. <br> Servicio de Registro de Usuarios</p>";
        emailMessage(subject, senderName, mailContent, mailSender, userEntity);
    }


    public void sendPasswordResetVerificationEmail(UserEntity userEntity, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Verificación de solicitud de restablecimiento de contraseña";
        String senderName = "Servicio de Verificación de Usuarios";
        String mailContent = "<p> ¡Hola, " + userEntity.getName() + "!</p>" +
                "<p><b>Recientemente solicitaste restablecer tu contraseña,</b>" + "" +
                "Por favor, sigue el enlace a continuación para completar la acción.</p>" +
                "<a href=\"" + url + "\">Restablecer contraseña</a>" +
                "<p> Servicio de Registro de Usuarios</p>";
        emailMessage(subject, senderName, mailContent, mailSender, userEntity);
    }

    private static void emailMessage(String subject, String senderName, String mailContent, JavaMailSender mailSender, UserEntity theUser) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("sjcexe@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }


}
