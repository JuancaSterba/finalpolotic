package com.polotic.taskmanager.dao;

import com.polotic.taskmanager.model.Task;
import com.polotic.taskmanager.model.TaskStatus;
import com.polotic.taskmanager.security.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ITaskDAO extends JpaRepository<Task, Long> {

    List<Task> findByUser(UserEntity user);

    @Query("SELECT t FROM Task t WHERE t.expirationDate < :now AND t.status = :taskStatus")
    List<Task> findExpiredTasks(@Param("now") LocalDate now, @Param("taskStatus") TaskStatus taskStatus);

}