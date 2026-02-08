package com.bookers.backend.domain.like.entity;

import com.bookers.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes", uniqueConstraints = {
        // 한 유저가 같은 대상(예: 게시글 1번)에 좋아요를 두 번 누를 수 없게 막음 (DB 레벨 방어)
        @UniqueConstraint(columnNames = {"user_id", "target_type", "target_id"})
})
public class Like {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 좋아요 누른 사람

    @Enumerated(EnumType.STRING) // ✅ 방금 만든 Enum 적용!
    private TargetType targetType; // 대상 종류 (POST, COMMENT, SHELF)

    private Long targetId; // 대상의 ID (PK)

    @Builder
    public Like(User user, TargetType targetType, Long targetId) {
        this.user = user;
        this.targetType = targetType;
        this.targetId = targetId;
    }
}