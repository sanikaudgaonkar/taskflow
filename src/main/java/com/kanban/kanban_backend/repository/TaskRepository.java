package com.kanban.kanban_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kanban.kanban_backend.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByColumnId(Long columnId);
}