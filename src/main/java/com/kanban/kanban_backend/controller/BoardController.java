package com.kanban.kanban_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanban.kanban_backend.dto.BoardRequest;
import com.kanban.kanban_backend.entity.Board;
import com.kanban.kanban_backend.entity.BoardColumn;
import com.kanban.kanban_backend.repository.BoardColumnRepository;
import com.kanban.kanban_backend.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BoardController {

    private final BoardService boardService;
    private final BoardColumnRepository boardColumnRepository;

    @PostMapping
    public ResponseEntity<Board> createBoard(
            @RequestBody BoardRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(boardService.createBoard(request, userDetails.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<Board>> getBoards(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(boardService.getUserBoards(userDetails.getUsername()));
    }

    @GetMapping("/{boardId}/columns")
    public ResponseEntity<List<BoardColumn>> getColumns(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardColumnRepository.findByBoardId(boardId));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().build();
    }
}