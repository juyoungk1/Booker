package com.bookers.backend.domain.book.entity;

import com.bookers.backend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // ISBN은 고유해야 함
    private String isbn13;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String cover;     // 표지 이미지 URL
    private String publisher; // 출판사

    @Builder
    public Book(String isbn13, String title, String author, String cover, String publisher) {
        this.isbn13 = isbn13;
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.publisher = publisher;
    }
}