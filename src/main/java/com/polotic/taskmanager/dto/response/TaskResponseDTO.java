package com.polotic.taskmanager.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String priority;
    private String status;
    private LocalDate creationDate;
    private LocalDate expirationDate;

}
