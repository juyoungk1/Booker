package com.bookers.backend.domain.book.repository;

import com.bookers.backend.domain.book.dto.ShelfStatDto;
import com.bookers.backend.domain.book.entity.MyBook;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
    // 이미 내 서재에 담은 책인지 확인용
    boolean existsByUserIdAndBookIsbn13(Long userId, String isbn13);

    // [추가] 내 서재 목록 가져오기 (최신순 정렬)
    List<MyBook> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    // JPQL로 한방에 통계 내기 (GROUP BY 활용)
    @Query("SELECT m.genre as genre, COUNT(m) as count " +
            "FROM MyBook m " +
            "WHERE m.user.id = :userId " +
            "GROUP BY m.genre")
    List<Tuple> getGenreStats(@Param("userId") Long userId);
    }