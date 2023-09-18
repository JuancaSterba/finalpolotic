package com.polotic.taskmanager.controller;

import com.polotic.taskmanager.dto.UserEntityDTO;
import com.polotic.taskmanager.dto.response.TaskResponseDTO;
import com.polotic.taskmanager.exception.BadRequestException;
import com.polotic.taskmanager.model.Task;
import com.polotic.taskmanager.security.user.IUserService;
import com.polotic.taskmanager.security.user.UserEntity;
import com.polotic.taskmanager.service.ITaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {

    private final ITaskService taskService;
    private final IUserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/")
    public String index(Model model, Principal principal, @AuthenticationPrincipal UserDetails userDetails) {

        if (principal != null) {
            String userEmail = principal.getName();
            Optional<UserEntity> userOptional = userService.findByEmail(userEmail);

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                List<TaskResponseDTO> taskDTOs = getTaskDTOsByUser(user);

                model.addAttribute("userTasks", taskDTOs);
                model.addAttribute("vista", "fragments/cards");
                model.addAttribute("titulo", "Home");
            }
        } else {
            model.addAttribute("vista", "index");
            model.addAttribute("titulo", "Inicio");
        }

        model.addAttribute("userDetails", userDetails);

        return "fragments/base";
    }

    @GetMapping("/task/table")
    public String showTaskTable(Model model, Principal principal, @AuthenticationPrincipal UserDetails userDetails) {
        if (principal != null) {
            String userEmail = principal.getName();
            Optional<UserEntity> userOptional = userService.findByEmail(userEmail);

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                List<TaskResponseDTO> taskDTOs = getTaskDTOsByUser(user);

                model.addAttribute("tasks", taskDTOs);
                model.addAttribute("vista", "fragments/table");
                model.addAttribute("titulo", "Lista de tareas");
            }
        }

        model.addAttribute("userDetails", userDetails);

        return "fragments/base";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("vista", "user/login");
        model.addAttribute("titulo", "Login");
        return "fragments/base";
    }

    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("vista", "error/error");
        model.addAttribute("titulo", "Error");
        return "fragments/base";
    }

    private List<TaskResponseDTO> getTaskDTOsByUser(UserEntity user) {
        List<Task> tasks = taskService.findByUser(user);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskResponseDTO.class))
                .collect(Collectors.toList());
    }

}
