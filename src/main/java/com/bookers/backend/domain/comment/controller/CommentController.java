package com.bookers.backend.domain.comment.controller;

import com.bookers.backend.domain.comment.dto.CommentRequest;
import com.bookers.backend.domain.comment.dto.CommentResponse;
import com.bookers.backend.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 1. 댓글 작성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<String> writeComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        commentService.writeComment(postId, email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("댓글 작성 성공!");
    }

    // 2. 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getComments(postId));
    }

    // 3. 댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<String> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        commentService.updateComment(commentId, email, request);
        return ResponseEntity.ok("댓글 수정 성공!");
    }

    // 4. 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        commentService.deleteComment(commentId, email);
        return ResponseEntity.ok("댓글 삭제 성공!");
    }
}