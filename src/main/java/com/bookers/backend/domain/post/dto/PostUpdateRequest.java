package com.bookers.backend.domain.post.dto;

import com.bookers.backend.domain.post.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostUpdateRequest(
        @NotBlank(message = "제목을 입력해주세요.")
        String title,

        @NotBlank(message = "내용을 입력해주세요.")
        String content,

        @NotNull(message = "카테고리를 선택해주세요.")
        Category category
) {}