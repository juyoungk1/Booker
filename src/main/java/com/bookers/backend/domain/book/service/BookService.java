package com.bookers.backend.domain.book.service;

import com.bookers.backend.domain.book.dto.BookSaveRequest;
import com.bookers.backend.domain.book.dto.MyBookUpdateRequest;
import com.bookers.backend.domain.book.entity.Book;
import com.bookers.backend.domain.book.entity.MyBook;
import com.bookers.backend.domain.book.repository.BookRepository;
import com.bookers.backend.domain.book.repository.MyBookRepository;
import com.bookers.backend.domain.user.entity.User;
import com.bookers.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bookers.backend.domain.book.dto.MyBookResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final MyBookRepository myBookRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long addBookToShelf(String email, BookSaveRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        // 1. 이미 내 서재에 있는 책인지 확인
        if (myBookRepository.existsByUserIdAndBookIsbn13(user.getId(), request.isbn13())) {
            throw new IllegalArgumentException("이미 서재에 담긴 책입니다.");
        }

        // 2. Book 테이블에 책이 있는지 확인 -> 없으면 저장 (단 한 번만 저장됨)
        Book book = bookRepository.findByIsbn13(request.isbn13())
                .orElseGet(() -> bookRepository.save(Book.builder()
                        .isbn13(request.isbn13())
                        .title(request.title())
                        .author(request.author())
                        .cover(request.cover())
                        .publisher(request.publisher())
                        .build()));

        // 3. 내 서재(MyBook)에 연결
        MyBook myBook = MyBook.builder()
                .user(user)
                .book(book)
                .status(request.status())
                .build();

        return myBookRepository.save(myBook).getId();
    }

    // 내 서재 조회
    public List<MyBookResponse> getMyBooks(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        return myBookRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(MyBookResponse::from)
                .collect(Collectors.toList());
    }

    // 3. 내 서재 책 수정 (상태/별점)
    @Transactional
    public void updateMyBook(Long myBookId, String email, MyBookUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        MyBook myBook = myBookRepository.findById(myBookId)
                .orElseThrow(() -> new IllegalArgumentException("서재에 담긴 책이 아닙니다."));

        // 내 책이 맞는지 확인
        if (!myBook.isOwner(user.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        myBook.update(request.status(), request.rating());
    }

    // 4. 내 서재에서 책 삭제
    @Transactional
    public void deleteMyBook(Long myBookId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        MyBook myBook = myBookRepository.findById(myBookId)
                .orElseThrow(() -> new IllegalArgumentException("서재에 담긴 책이 아닙니다."));

        if (!myBook.isOwner(user.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        myBookRepository.delete(myBook);
    }
}