package com.polotic.taskmanager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {

    private String title;
    private String description;
    private String priority;
    private String status;
    private LocalDate expirationDate;
    private Long userId;

}
