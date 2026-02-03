package com.bookers.backend.domain.book.entity;

import com.bookers.backend.domain.user.entity.User;
import com.bookers.backend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyBook extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private BookStatus status; // 읽는 중, 다 읽음 등

    private Integer rating; // 별점 (0~5), 나중에 사용

    @Builder
    public MyBook(User user, Book book, BookStatus status) {
        this.user = user;
        this.book = book;
        this.status = status;
    }

    // [추가] 상태 및 별점 수정
    public void update(BookStatus status, Integer rating) {
        this.status = status;
        this.rating = rating;
    }

    // [추가] 소유자 확인 메서드
    public boolean isOwner(Long userId) {
        return this.user.getId().equals(userId);
    }
}