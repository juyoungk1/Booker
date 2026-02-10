package com.bookers.backend.domain.book.service;

import com.bookers.backend.domain.book.dto.BookMemoRequest;
import com.bookers.backend.domain.book.dto.BookMemoResponse;
import com.bookers.backend.domain.book.entity.BookMemo;
import com.bookers.backend.domain.book.entity.MyBook;
import com.bookers.backend.domain.book.repository.BookMemoRepository;
import com.bookers.backend.domain.book.repository.MyBookRepository;
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
public class BookMemoService {

    private final BookMemoRepository bookMemoRepository;
    private final MyBookRepository myBookRepository;
    private final UserRepository userRepository;

    // 1. 메모 추가
    @Transactional
    public Long addMemo(Long myBookId, String email, BookMemoRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        MyBook myBook = myBookRepository.findById(myBookId)
                .orElseThrow(() -> new IllegalArgumentException("책이 존재하지 않습니다."));

        // 내 책이 맞는지 확인 (남의 책에 메모 쓰면 안 되니까)
        if (!myBook.isOwner(user.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        BookMemo memo = BookMemo.builder()
                .myBook(myBook)
                .content(request.content())
                .page(request.page())
                .build();

        return bookMemoRepository.save(memo).getId();
    }

    // 2. 특정 책의 메모 목록 조회 (슬라이드용)
    public List<BookMemoResponse> getMemos(Long myBookId, String email) {
        // (공개 범위 체크 로직이 필요하다면 여기서 추가 가능)
        // 일단은 내 책 메모만 보는 걸로 가정
        return bookMemoRepository.findAllByMyBookIdOrderByCreatedAtAsc(myBookId)
                .stream()
                .map(BookMemoResponse::from)
                .collect(Collectors.toList());
    }

    // 3. 메모 수정
    @Transactional
    public void updateMemo(Long memoId, String email, BookMemoRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        BookMemo memo = bookMemoRepository.findById(memoId)
                .orElseThrow(() -> new IllegalArgumentException("메모가 없습니다."));

        if (!memo.getMyBook().isOwner(user.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        memo.update(request.content(), request.page());
    }

    // 4. 메모 삭제
    @Transactional
    public void deleteMemo(Long memoId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        BookMemo memo = bookMemoRepository.findById(memoId)
                .orElseThrow(() -> new IllegalArgumentException("메모가 없습니다."));

        if (!memo.getMyBook().isOwner(user.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        bookMemoRepository.delete(memo);
    }
}