package com.bookers.backend.domain.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// 알라딘이 주는 불필요한 필드는 무시(@JsonIgnoreProperties)
@JsonIgnoreProperties(ignoreUnknown = true)
public record AladinSearchResponse(
        // 알라딘은 "item"이라는 리스트 안에 책 정보들을 줍니다.
        List<Item> item
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Item(
            String title,
            String author,
            String publisher,
            String pubDate, // 출판일
            String description, // 책 소개
            String isbn13, // ISBN (고유번호)
            String cover,  // 표지 이미지 URL
            @JsonProperty("link") String link // 알라딘 상세 페이지 링크
    ) {}
}