package com.bookers.backend.domain.book.repository;

import com.bookers.backend.domain.book.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
    // 이미 내 서재에 담은 책인지 확인용
    boolean existsByUserIdAndBookIsbn13(Long userId, String isbn13);

    // [추가] 내 서재 목록 가져오기 (최신순 정렬)
    List<MyBook> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}