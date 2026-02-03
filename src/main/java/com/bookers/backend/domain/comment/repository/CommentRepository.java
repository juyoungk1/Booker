package com.bookers.backend.domain.comment.repository;

import com.bookers.backend.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글의 댓글을 작성일 순(오래된 순)으로 가져오기
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
}