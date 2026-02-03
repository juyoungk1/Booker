package com.bookers.backend.domain.book.dto;

import com.bookers.backend.domain.book.entity.BookStatus;
import jakarta.validation.constraints.NotNull;

public record MyBookUpdateRequest(
        @NotNull(message = "변경할 상태를 선택해주세요.")
        BookStatus status, // READING, DONE 등

        Integer rating // 별점 (0~5), 선택사항(null 가능)
) {}