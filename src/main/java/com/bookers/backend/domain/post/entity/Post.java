package com.bookers.backend.domain.post.entity;

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
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    // 작성자 (User)와 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Post(String title, String content, Category category, User user) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.user = user;
    }

    public void update(String title, String content, Category category){
        this.title=title;
        this.content=content;
        this.category=category;
    }

    public boolean isAuthor(String email){
        return this.user.getEmail().equals(email);
    }
}