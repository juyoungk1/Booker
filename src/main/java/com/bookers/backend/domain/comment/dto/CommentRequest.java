package com.bookers.backend.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
        @NotBlank(message = "댓글 내용을 입력해주세요.")
        String content
) {}