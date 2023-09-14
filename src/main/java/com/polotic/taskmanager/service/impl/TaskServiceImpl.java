package com.polotic.taskmanager.service.impl;

import com.polotic.taskmanager.dao.ITaskDAO;
import com.polotic.taskmanager.dto.UserEntityDTO;
import com.polotic.taskmanager.dto.request.TaskRequestDTO;
import com.polotic.taskmanager.dto.response.TaskResponseDTO;
import com.polotic.taskmanager.model.Task;
import com.polotic.taskmanager.model.TaskPriority;
import com.polotic.taskmanager.model.TaskStatus;
import com.polotic.taskmanager.security.user.UserEntity;
import com.polotic.taskmanager.security.user.UserRepository;
import com.polotic.taskmanager.service.ITaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements ITaskService {

    private final ITaskDAO taskDAO;
    private final UserRepository userRepository;


    @Override
    public List<Task> findByUser(UserEntity user) {
        return taskDAO.findByUser(user);
    }

    @Override
    public void save(TaskRequestDTO task, Long userId) {
        Task newTask = new Task();
        UserEntity user = userRepository.findById(userId).get();

        newTask.setTitle(task.getTitle());
        newTask.setDescription(task.getDescription());
        newTask.setCreationDate(LocalDate.now());
        newTask.setExpirationDate(task.getExpirationDate());
        newTask.setPriority(TaskPriority.valueOf(task.getPriority()));
        newTask.setStatus(TaskStatus.PENDIENTE);
        newTask.setUser(user);

        taskDAO.save(newTask);
    }

    @Override
    public Task findById(Long id) {
        return taskDAO.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        taskDAO.deleteById(id);
    }

    @Override
    public void update(TaskRequestDTO task, Long taskId) {
        Task existingTask = taskDAO.findById(taskId).orElse(null);
        if (existingTask != null) {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setExpirationDate(task.getExpirationDate());
            existingTask.setPriority(TaskPriority.valueOf(task.getPriority()));
            taskDAO.save(existingTask);
        }
    }

    @Override
    public void doneTask(Long id) {
        Task existingTask = taskDAO.findById(id).orElse(null);
        if (existingTask != null) {
            existingTask.setStatus(TaskStatus.TERMINADO);
            taskDAO.save(existingTask);
        }
    }

}
