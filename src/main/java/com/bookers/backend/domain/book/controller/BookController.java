package com.bookers.backend.domain.book.controller;

import com.bookers.backend.domain.book.dto.*;
import com.bookers.backend.domain.book.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookers.backend.domain.book.service.BookService;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookSearchService bookSearchService;
    private final BookService bookService; // 생성자 주입 자동 처리됨 (@RequiredArgsConstructor)

    // 도서 검색 API (로그인 안 해도 검색 가능하게 설정 필요!)
    @GetMapping("/search")
    public ResponseEntity<List<AladinSearchResponse.Item>> search(@RequestParam String query) {
        List<AladinSearchResponse.Item> results = bookSearchService.searchBooks(query);
        return ResponseEntity.ok(results);
    }

    // 내 서재에 책 담기
    @PostMapping("/shelf")
    public ResponseEntity<String> addBookToShelf(
            @Valid @RequestBody BookSaveRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        bookService.addBookToShelf(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("서재에 책을 담았습니다.");
    }

    // 내 서재 조회 API
    @GetMapping("/shelf")
    public ResponseEntity<List<MyBookResponse>> getMyBooks(Authentication authentication) {
        String email = authentication.getName();
        List<MyBookResponse> myBooks = bookService.getMyBooks(email);
        return ResponseEntity.ok(myBooks);
    }

    // 3. 내 서재 책 수정 (상태 변경 등)
    @PatchMapping("/shelf/{myBookId}")
    public ResponseEntity<String> updateMyBook(
            @PathVariable Long myBookId,
            @Valid @RequestBody MyBookUpdateRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        bookService.updateMyBook(myBookId, email, request);
        return ResponseEntity.ok("도서 상태가 변경되었습니다.");
    }

    // 4. 내 서재에서 책 삭제
    @DeleteMapping("/shelf/{myBookId}")
    public ResponseEntity<String> deleteMyBook(
            @PathVariable Long myBookId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        bookService.deleteMyBook(myBookId, email);
        return ResponseEntity.ok("서재에서 도서를 삭제했습니다.");
    }

    // [신규] 내 서재 통계 (분야별 권수 & 퍼센트)
    @GetMapping("/shelf/stats")
    public ResponseEntity<List<ShelfStatDto>> getMyShelfStats(Authentication authentication) {
        return ResponseEntity.ok(bookService.getMyShelfStats(authentication.getName()));
    }



}