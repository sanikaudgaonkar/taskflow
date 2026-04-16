package com.kanban.kanban_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kanban.kanban_backend.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUserId(Long userId);
}