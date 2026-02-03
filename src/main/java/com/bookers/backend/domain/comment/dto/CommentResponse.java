package com.bookers.backend.domain.comment.dto;

import com.bookers.backend.domain.comment.entity.Comment;
import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String author, // 작성자 닉네임
        LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getNickname(),
                comment.getCreatedAt()
        );
    }
}