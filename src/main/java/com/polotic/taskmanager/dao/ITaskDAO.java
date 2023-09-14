package com.polotic.taskmanager.dao;

import com.polotic.taskmanager.model.Task;
import com.polotic.taskmanager.security.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskDAO extends JpaRepository<Task, Long> {
    List<Task> findByUser(UserEntity user);

}