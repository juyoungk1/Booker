package com.bookers.backend.domain.book.dto;

import com.bookers.backend.domain.book.entity.BookGenre;
import com.bookers.backend.domain.book.entity.BookStatus;
import com.bookers.backend.domain.book.entity.MyBook;
import com.bookers.backend.domain.book.entity.Visibility;

import java.time.LocalDateTime;

public record MyBookResponse(
        Long myBookId,      // 내 서재 ID
        String isbn13,      // 책 고유 번호
        String title,       // 제목
        String author,      // 저자
        String cover,       // 표지 이미지
        String publisher,   // 출판사
        BookStatus status,  // 독서 상태 (READING, DONE 등)
        Integer rating,     // 별점

        // 👇 [추가] 여기 3개를 추가해야 조회가 됩니다!
        BookGenre genre,
        Visibility visibility,
        String memo,

        LocalDateTime createdAt // 담은 날짜
) {
    // Entity -> DTO 변환 메서드
    public static MyBookResponse from(MyBook myBook) {
        return new MyBookResponse(
                myBook.getId(),
                myBook.getBook().getIsbn13(),
                myBook.getBook().getTitle(),
                myBook.getBook().getAuthor(),
                myBook.getBook().getCover(),
                myBook.getBook().getPublisher(),
                myBook.getStatus(),
                myBook.getRating(),

                // 👇 [추가] 엔티티에서 꺼내서 담기
                myBook.getGenre(),
                myBook.getVisibility(),
                myBook.getMemo(),

                myBook.getCreatedAt()
        );
    }
}