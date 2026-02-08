package com.bookers.backend.domain.like.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TargetType {
    POST("게시글"),
    COMMENT("댓글"),
    SHELF("서재");

    private final String description;
}