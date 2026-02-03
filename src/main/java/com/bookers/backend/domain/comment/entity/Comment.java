package com.bookers.backend.domain.comment.entity;

import com.bookers.backend.domain.post.entity.Post;
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
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500) // 댓글은 너무 길지 않게 제한
    private String content;

    // 작성자 연결 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 게시글 연결 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }

    // 댓글 수정 메서드
    public void update(String content) {
        this.content = content;
    }

    // 작성자 확인 편의 메서드
    public boolean isAuthor(String email) {
        return this.user.getEmail().equals(email);
    }
}