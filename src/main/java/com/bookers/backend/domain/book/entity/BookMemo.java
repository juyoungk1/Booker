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

    // (내 서재의 책과 연결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_book_id")
    private MyBook myBook;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 메모 내용 (명언, 생각 등)

    private Integer pageNumber; // (선택) 몇 페이지 읽고 쓴 건지?

    @Builder
    public BookMemo(MyBook myBook, String content, Integer pageNumber) {
        this.myBook = myBook;
        this.content = content;
        this.pageNumber = pageNumber;
    }

    // 메모 수정 기능
    public void update(String content, Integer pageNumber) {
        this.content = content;
        this.pageNumber = pageNumber;
    }
}