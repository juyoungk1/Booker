package com.bookers.backend.domain.post.controller;

import com.bookers.backend.domain.post.dto.PostCreateRequest;
import com.bookers.backend.domain.post.dto.PostUpdateRequest;
import com.bookers.backend.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.bookers.backend.domain.post.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> writePost(
            @Valid @RequestBody PostCreateRequest request,
            Authentication authentication // 필터에서 저장한 인증 객체
    ) {
        // 필터가 넣어둔 이메일 꺼내기 (Principal)
        String email = authentication.getName();

        postService.writePost(email, request);

        return ResponseEntity.status(HttpStatus.CREATED).body("게시글 작성 성공!");
    }

    // 1. 게시글 목록 조회 API
    @GetMapping
    public ResponseEntity<Page<PostResponse>> getPostList(
            // page: 페이지 번호(0부터 시작), size: 한 번에 가져올 개수, sort: 정렬 기준
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PostResponse> list = postService.getPostList(pageable);
        return ResponseEntity.ok(list);
    }

    // 2. 게시글 상세 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        PostResponse response = postService.getPost(id);
        return ResponseEntity.ok(response);
    }

    // 3. 게시글 수정 API
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        postService.updatePost(id, email, request);
        return ResponseEntity.ok("게시글이 수정되었습니다.");
    }

    // 4. 게시글 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String email = authentication.getName();
        postService.deletePost(id, email);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}