package com.bookers.backend.domain.book.dto;

import com.bookers.backend.domain.book.entity.BookStatus; // 또는 ReadStatus
import com.bookers.backend.domain.book.entity.Visibility;
import jakarta.validation.constraints.NotNull;

public record MyBookUpdateRequest(
        @NotNull(message = "변경할 상태를 선택해주세요.")
        BookStatus status, // READING, DONE 등

        Integer rating,    // 별점 (0~5)

        // 👇 추가된 필드들 (이미 잘 넣으셨습니다!)
        String memo,
        Visibility visibility,

        Integer totalPage,
        Integer currentPage
) {}