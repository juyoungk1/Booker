package com.bookers.backend.domain.post.dto;

import com.bookers.backend.domain.post.entity.Category;
import com.bookers.backend.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String content,
        Category category,
        String author, // 작성자 닉네임만 보여줌
        int viewCount,
        LocalDateTime createdAt
) {
    // Entity -> DTO 변환을 위한 static 메서드 (편의성)
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCategory(),
                post.getUser().getNickname(), // User 엔티티에서 닉네임 꺼내기
                0, // 조회수는 일단 0 (나중에 추가)
                post.getCreatedAt()
        );
    }
}