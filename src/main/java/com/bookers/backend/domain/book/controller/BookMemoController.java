package com.bookers.backend.domain.book.controller;

import com.bookers.backend.domain.book.dto.BookMemoRequest;
import com.bookers.backend.domain.book.dto.BookMemoResponse;
import com.bookers.backend.domain.book.service.BookMemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookMemoController {

    private final BookMemoService bookMemoService;

    // 1. 메모 추가 (POST /api/books/shelf/{myBookId}/memos)
    @PostMapping("/shelf/{myBookId}/memos")
    public ResponseEntity<Long> addMemo(
            @PathVariable Long myBookId,
            @RequestBody BookMemoRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(bookMemoService.addMemo(myBookId, authentication.getName(), request));
    }

    // 2. 이 책의 메모 목록 조회 (GET /api/books/shelf/{myBookId}/memos)
    @GetMapping("/shelf/{myBookId}/memos")
    public ResponseEntity<List<BookMemoResponse>> getMemos(
            @PathVariable Long myBookId,
            Authentication authentication) {
        return ResponseEntity.ok(bookMemoService.getMemos(myBookId, authentication.getName()));
    }

    // 3. 메모 수정 (PATCH /api/books/memos/{memoId})
    @PatchMapping("/memos/{memoId}")
    public ResponseEntity<Void> updateMemo(
            @PathVariable Long memoId,
            @RequestBody BookMemoRequest request,
            Authentication authentication) {
        bookMemoService.updateMemo(memoId, authentication.getName(), request);
        return ResponseEntity.ok().build();
    }

    // 4. 메모 삭제 (DELETE /api/books/memos/{memoId})
    @DeleteMapping("/memos/{memoId}")
    public ResponseEntity<Void> deleteMemo(
            @PathVariable Long memoId,
            Authentication authentication) {
        bookMemoService.deleteMemo(memoId, authentication.getName());
        return ResponseEntity.ok().build();
    }
}