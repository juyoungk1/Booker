package com.bookers.backend.domain.book.dto;

import com.bookers.backend.domain.book.entity.BookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookSaveRequest(
        @NotBlank String isbn13,
        @NotBlank String title,
        @NotBlank String author,
        String cover,
        String publisher,

        @NotNull(message = "독서 상태를 선택해주세요 (READING, DONE, WISH)")
        BookStatus status
) {}