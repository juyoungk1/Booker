package com.bookers.backend.domain.book.dto;

import com.bookers.backend.domain.book.entity.BookGenre; // 임포트 필수!

public record ShelfStatDto(
        BookGenre genre, // ❌ String -> ✅ BookGenre 로 변경
        Long count,      // COUNT의 결과는 무조건 Long입니다. (int 쓰면 에러남)
        Double percentage
) {}