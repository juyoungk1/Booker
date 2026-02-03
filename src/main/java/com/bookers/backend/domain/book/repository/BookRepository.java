package com.bookers.backend.domain.book.repository;

import com.bookers.backend.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn13(String isbn13);
}