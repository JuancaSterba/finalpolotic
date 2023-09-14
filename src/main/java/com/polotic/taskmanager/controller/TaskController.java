package com.polotic.taskmanager.controller;

import com.polotic.taskmanager.dto.request.TaskRequestDTO;
import com.polotic.taskmanager.model.Task;
import com.polotic.taskmanager.model.TaskPriority;
import com.polotic.taskmanager.security.user.IUserService;
import com.polotic.taskmanager.service.ITaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final ITaskService taskService;
    private final IUserService userService;

    // crear
    @GetMapping
    public String showTaskForm(Model model) {
        TaskRequestDTO task = new TaskRequestDTO();
        List<TaskPriority> opciones = List.of(TaskPriority.values());
        model.addAttribute("task", task);
        model.addAttribute("opciones", opciones);
        model.addAttribute("vista", "fragments/newTask");
        model.addAttribute("titulo", "Nueva tarea");
        return "fragments/base";
    }

    // guardar
    @PostMapping("/save")
    public String saveTask(@Valid TaskRequestDTO task, BindingResult bindingResult, Model model, Principal principal) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("task", task);
            return this.showTaskForm(model);
        }

        Long userId = userService.findByEmail(principal.getName()).get().getId();
        taskService.save(task, userId);
        return "redirect:/";

    }


    // editar
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Task task = taskService.findById(id);
        List<TaskPriority> opciones = List.of(TaskPriority.values());
        model.addAttribute("task", task);
        model.addAttribute("opciones", opciones);
        model.addAttribute("vista", "fragments/editTask");
        model.addAttribute("titulo", "Editar tarea");
        return "fragments/base";
    }

    @PutMapping("/edit/{id}")
    public String updateTask(@PathVariable("id") Long id, @Valid TaskRequestDTO task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("task", task);
            return this.showTaskForm(model);
        }
        taskService.update(task, id); // Llamar al método update en el servicio
        return "redirect:/";
    }


    // eliminar
    @DeleteMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        taskService.deleteById(id);
        return "redirect:/";
    }


    // cambiar de estado
    @GetMapping("/done/{id}")
    public String doneTask(@PathVariable("id") Long id) {
        taskService.doneTask(id);
        return "redirect:/";
    }

}
