package com.bookers.backend.domain.book.repository;

import com.bookers.backend.domain.book.entity.BookMemo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookMemoRepository extends JpaRepository<BookMemo, Long> {
    // 특정 내 책(MyBook)에 달린 메모를 작성된 순서대로 가져오기
    List<BookMemo> findAllByMyBookIdOrderByCreatedAtAsc(Long myBookId);
}