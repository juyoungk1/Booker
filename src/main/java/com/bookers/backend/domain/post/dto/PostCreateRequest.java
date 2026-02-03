package com.bookers.backend.domain.post.dto;

import com.bookers.backend.domain.post.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostCreateRequest(
        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @NotBlank(message = "내용은 필수입니다.")
        String content,

        @NotNull(message = "카테고리를 선택해주세요.")
        Category category
) {}