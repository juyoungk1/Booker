package com.bookers.backend.domain.book.dto;

import com.bookers.backend.domain.book.entity.BookMemo;
import java.time.LocalDateTime;

public record BookMemoResponse(
        Long memoId,
        String content,
        Integer page,
        LocalDateTime createdAt
) {
    public static BookMemoResponse from(BookMemo memo) {
        return new BookMemoResponse(
                memo.getId(),
                memo.getContent(),
                memo.getPage(),
                memo.getCreatedAt()
        );
    }
}