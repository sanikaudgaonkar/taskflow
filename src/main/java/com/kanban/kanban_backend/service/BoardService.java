package com.kanban.kanban_backend.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kanban.kanban_backend.dto.BoardRequest;
import com.kanban.kanban_backend.entity.Board;
import com.kanban.kanban_backend.entity.BoardColumn;
import com.kanban.kanban_backend.entity.User;
import com.kanban.kanban_backend.repository.BoardColumnRepository;
import com.kanban.kanban_backend.repository.BoardRepository;
import com.kanban.kanban_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final UserRepository userRepository;

    public Board createBoard(BoardRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Board board = new Board();
        board.setTitle(request.getTitle());
        board.setUser(user);
        Board savedBoard = boardRepository.save(board);

        // Auto-create 3 default columns
        List<String> columnNames = Arrays.asList("To Do", "In Progress", "Done");
        for (int i = 0; i < columnNames.size(); i++) {
            BoardColumn column = new BoardColumn();
            column.setName(columnNames.get(i));
            column.setPosition(i);
            column.setBoard(savedBoard);
            boardColumnRepository.save(column);
        }

        return savedBoard;
    }

    public List<Board> getUserBoards(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return boardRepository.findByUserId(user.getId());
    }

    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }
}