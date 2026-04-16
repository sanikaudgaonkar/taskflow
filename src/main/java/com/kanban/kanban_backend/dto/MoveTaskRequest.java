package com.kanban.kanban_backend.dto;

import lombok.Data;

@Data
public class MoveTaskRequest {
    private Long newColumnId;
}