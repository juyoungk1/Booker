package com.bookers.backend.domain.book.dto;

import jakarta.validation.constraints.NotBlank;

public record BookMemoRequest(
        @NotBlank(message = "내용을 입력해주세요,")
        String content,

        Integer page //페이지는 비워도됨(null  가능)
) {
}
