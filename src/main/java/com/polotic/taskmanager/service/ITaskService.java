package com.polotic.taskmanager.service;

import com.polotic.taskmanager.dto.request.TaskRequestDTO;
import com.polotic.taskmanager.model.Task;
import com.polotic.taskmanager.security.user.UserEntity;

import java.util.List;

public interface ITaskService {

    List<Task> findByUser(UserEntity user);

    public void save(TaskRequestDTO task, Long userId);

    public Task findById(Long id);

    public void deleteById(Long id);

    public void update(TaskRequestDTO task, Long taskId);

    void doneTask(Long id);
}
