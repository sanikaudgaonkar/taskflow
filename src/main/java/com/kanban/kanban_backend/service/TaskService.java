package com.kanban.kanban_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kanban.kanban_backend.dto.MoveTaskRequest;
import com.kanban.kanban_backend.dto.TaskRequest;
import com.kanban.kanban_backend.entity.BoardColumn;
import com.kanban.kanban_backend.entity.Task;
import com.kanban.kanban_backend.entity.TaskStatus;
import com.kanban.kanban_backend.repository.BoardColumnRepository;
import com.kanban.kanban_backend.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final BoardColumnRepository boardColumnRepository;

    public Task createTask(TaskRequest request) {
        BoardColumn column = boardColumnRepository.findById(request.getColumnId())
                .orElseThrow(() -> new RuntimeException("Column not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setPriority(request.getPriority() != null ? request.getPriority() : "MEDIUM");
        task.setStatus(TaskStatus.TODO);
        task.setColumn(column);

        return taskRepository.save(task);
    }

    public List<Task> getTasksByColumn(Long columnId) {
        return taskRepository.findByColumnId(columnId);
    }

    public Task moveTask(Long taskId, MoveTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        BoardColumn newColumn = boardColumnRepository.findById(request.getNewColumnId())
                .orElseThrow(() -> new RuntimeException("Column not found"));

        task.setColumn(newColumn);

        String columnName = newColumn.getName();
        if (columnName.equals("To Do")) task.setStatus(TaskStatus.TODO);
        else if (columnName.equals("In Progress")) task.setStatus(TaskStatus.IN_PROGRESS);
        else if (columnName.equals("Done")) task.setStatus(TaskStatus.DONE);

        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, TaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }

        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}