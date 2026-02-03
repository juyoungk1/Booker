package com.bookers.backend.domain.comment.service;

import com.bookers.backend.domain.comment.dto.CommentRequest;
import com.bookers.backend.domain.comment.dto.CommentResponse;
import com.bookers.backend.domain.comment.entity.Comment;
import com.bookers.backend.domain.comment.repository.CommentRepository;
import com.bookers.backend.domain.post.entity.Post;
import com.bookers.backend.domain.post.repository.PostRepository;
import com.bookers.backend.domain.user.entity.User;
import com.bookers.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 1. 댓글 작성
    @Transactional
    public Long writeComment(Long postId, String email, CommentRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .content(request.content())
                .user(user)
                .post(post)
                .build();

        return commentRepository.save(comment).getId();
    }

    // 2. 댓글 목록 조회
    public List<CommentResponse> getComments(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    // 3. 댓글 수정
    @Transactional
    public void updateComment(Long commentId, String email, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!comment.isAuthor(email)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        comment.update(request.content());
    }

    // 4. 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!comment.isAuthor(email)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}