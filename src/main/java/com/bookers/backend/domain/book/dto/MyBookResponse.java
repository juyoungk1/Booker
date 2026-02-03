package com.bookers.backend.domain.book.dto;

import com.bookers.backend.domain.book.entity.Book;
import com.bookers.backend.domain.book.entity.BookStatus;
import com.bookers.backend.domain.book.entity.MyBook;

public record MyBookResponse(
        Long myBookId,    // 나중에 삭제/수정할 때 필요함
        String title,
        String author,
        String cover,
        BookStatus status // WISH, READING, DONE
) {
    public static MyBookResponse from(MyBook myBook) {
        Book book = myBook.getBook();
        return new MyBookResponse(
                myBook.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCover(),
                myBook.getStatus()
        );
    }
}