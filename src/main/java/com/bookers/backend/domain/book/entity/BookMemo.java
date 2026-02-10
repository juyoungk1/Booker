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
public class BookMemo extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 책에 꽂힌 메모인지 (부모 책)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_book_id")
    private MyBook myBook;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 메모 내용

    private Integer page; // 몇 페이지인지 (선택 사항)

    @Builder
    public BookMemo(MyBook myBook, String content, Integer page) {
        this.myBook = myBook;
        this.content = content;
        this.page = page;
    }

    // 수정 메서드
    public void update(String content, Integer page) {
        this.content = content;
        this.page = page;
    }
}