package com.polotic.taskmanager.security.controller;

import com.polotic.taskmanager.security.event.RegistrationCompleteEvent;
import com.polotic.taskmanager.security.event.listener.RegistrationCompleteEventListener;
import com.polotic.taskmanager.security.dto.RegistrationRequest;
import com.polotic.taskmanager.security.password.IPasswordResetTokenService;
import com.polotic.taskmanager.security.token.VerificationToken;
import com.polotic.taskmanager.security.token.VerificationTokenService;
import com.polotic.taskmanager.security.user.IUserService;
import com.polotic.taskmanager.security.user.UserEntity;
import com.polotic.taskmanager.security.utility.UrlUtil;
import com.polotic.taskmanager.service.RecaptchaService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final IUserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenService tokenService;
    private final IPasswordResetTokenService passwordResetTokenService;
    private final RegistrationCompleteEventListener eventListener;
    private final RecaptchaService recaptchaService;

    @Value("${recaptcha.sitekey}")
    private String sitekey;


    @GetMapping("/form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("vista", "user/register");
        model.addAttribute("titulo", "Registro");
        model.addAttribute("sitekey", sitekey);
        model.addAttribute("user", new RegistrationRequest());
        return "fragments/base";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequest registration,
                               @RequestParam("g-recaptcha-response") String captcha,
                               HttpServletRequest request) {

        boolean captchaValid = recaptchaService.isValid(captcha);
        if (captchaValid) {
            UserEntity user = userService.registerUser(registration);
            publisher.publishEvent(new RegistrationCompleteEvent(user, UrlUtil.getApplicationUrl(request)));
            return "redirect:/registration/form?success";
        } else {
            return "redirect:/registration/form?error";
        }

    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        Optional<VerificationToken> theToken = tokenService.findByToken(token);
        if (theToken.isPresent() && theToken.get().getUser().isEnabled()) {
            return "redirect:/login?verified";
        }
        String verificationResult = tokenService.validateToken(token);
        switch (verificationResult.toLowerCase()) {
            case "expired":
                return "redirect:/error?expired";
            case "valid":
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }

    @GetMapping("/forgot-password-request")
    public String forgotPasswordForm(Model model) {
        model.addAttribute("vista", "user/forgot-password-form");
        model.addAttribute("titulo", "Solicitud de Restablecimiento de Contraseña");
        return "fragments/base";
    }

    @PostMapping("/forgot-password")
    public String resetPasswordRequest(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        Optional<UserEntity> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            return "redirect:/registration/forgot-password-request?not_found";
        }
        String passwordResetToken = UUID.randomUUID().toString();
        passwordResetTokenService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
        //send password reset verification email to the user
        String url = UrlUtil.getApplicationUrl(request) + "/registration/password-reset-form?token=" + passwordResetToken;
        try {
            eventListener.sendPasswordResetVerificationEmail(user.get(),url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/registration/forgot-password-request?success";
    }

    @GetMapping("/password-reset-form")
    public String passwordResetForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("vista", "user/password-reset-form");
        model.addAttribute("titulo", "Restablecer contraseña");
        model.addAttribute("token", token);
        return "fragments/base";
    }

    @PostMapping("/reset-password")
    public String resetPassword(HttpServletRequest request) {
        String theToken = request.getParameter("token");
        String password = request.getParameter("password");
        String tokenVerificationResult = passwordResetTokenService.validatePasswordResetToken(theToken);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")) {
            return "redirect:/error?invalid_token";
        }
        Optional<UserEntity> theUser = passwordResetTokenService.findUserByPasswordResetToken(theToken);
        if (theUser.isPresent()) {
            passwordResetTokenService.resetPassword(theUser.get(), password);
            return "redirect:/login?reset_success";
        }
        return "redirect:/error?not_found";
    }
}
