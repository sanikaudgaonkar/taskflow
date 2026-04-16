package com.kanban.kanban_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kanban.kanban_backend.entity.BoardColumn;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    List<BoardColumn> findByBoardId(Long boardId);
}