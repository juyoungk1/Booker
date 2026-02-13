package com.bookers.backend.domain.book.dto;

import com.bookers.backend.domain.book.entity.BookGenre;
import com.bookers.backend.domain.book.entity.BookStatus; // 또는 ReadStatus (본인 Enum 이름에 맞게!)
import com.bookers.backend.domain.book.entity.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookSaveRequest(
        @NotBlank String isbn13,
        @NotBlank String title,
        @NotBlank String author,
        String cover,
        String publisher,

        // 👇 추가된 필드들 (이미 잘 넣으셨습니다!)
        BookGenre genre,
        Visibility visibility,
        String memo,

        Integer totalPage,
        Integer currentPage,

        @NotNull(message = "독서 상태를 선택해주세요")
        BookStatus status
) {}