package com.bookers.backend.domain.post.service;

import com.bookers.backend.domain.post.dto.PostCreateRequest;
import com.bookers.backend.domain.post.dto.PostResponse;
import com.bookers.backend.domain.post.dto.PostUpdateRequest;
import com.bookers.backend.domain.post.entity.Post;
import com.bookers.backend.domain.post.repository.PostRepository;
import com.bookers.backend.domain.user.entity.User;
import com.bookers.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long writePost(String email, PostCreateRequest request) {
        // 1. 작성자(User) 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 2. 게시글 생성
        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .category(request.category())
                .user(user)
                .build();

        // 3. 저장
        return postRepository.save(post).getId();
    }

    // 1. 게시글 목록 조회 (페이징 + 카테고리 필터링은 나중에)
    public Page<PostResponse> getPostList(Pageable pageable) {
        // findAll(pageable)이 알아서 DB에서 LIMIT, OFFSET 쿼리를 날려줍니다.
        // map(PostResponse::from)을 통해 Entity 리스트를 DTO 리스트로 변환합니다.
        return postRepository.findAll(pageable)
                .map(PostResponse::from);
    }

    // 2. 게시글 상세 조회
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // TODO: 여기서 조회수 증가 로직을 넣을 수 있습니다.

        return PostResponse.from(post);
    }

    // 3. 게시글 수정
    @Transactional
    public void updatePost(Long id, String email, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 권한 체크 (본인이 아니면 예외 발생)
        if (!post.isAuthor(email)) {
            throw new IllegalArgumentException("수정 권한이 없습니다."); // 403으로 처리하면 더 좋음
        }

        // 더티 체킹(Dirty Checking): save를 안 불러도 트랜잭션이 끝날 때 변경 감지해서 DB 업데이트
        post.update(request.title(), request.content(), request.category());
    }

    // 4. 게시글 삭제
    @Transactional
    public void deletePost(Long id, String email) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 권한 체크
        if (!post.isAuthor(email)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }
}
