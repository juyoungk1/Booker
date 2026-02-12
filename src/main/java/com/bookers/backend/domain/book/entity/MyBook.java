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

    //분야 (철학, 경제/경영, 소설 등)
    @Enumerated(EnumType.STRING)
    private BookGenre genre;

    //공개 범위(전체공개, 친구공개, 비공개)
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    //한 줄 메모
    @Column(columnDefinition = "TEXT")
    private String memo;

    //독서진도율 추후 추가

    @Builder
    public MyBook(User user, Book book, BookStatus status, BookGenre genre, Visibility visibility, String memo) {
        this.user = user;
        this.book = book;
        this.status = status;
        this.genre = genre;
        this.visibility = visibility == null ? Visibility.PRIVATE : visibility; // 기본값 처리
        this.memo = memo;
    }

    // [추가] 상태 및 별점 수정
    public void update(BookStatus status, Integer rating, String memo, Visibility visibility) {
        this.status = status;
        this.rating = rating;
        this.memo = memo;
        this.visibility = visibility;
    }

    // [추가] 소유자 확인 메서드
    public boolean isOwner(Long userId) {
        return this.user.getId().equals(userId);
    }


}