package com.kanban.kanban_backend.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TaskRequest {
    private String title;
    private String description;
    private LocalDate dueDate;
    private String priority;
    private Long columnId;
}