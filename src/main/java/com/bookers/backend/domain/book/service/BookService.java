package com.bookers.backend.domain.book.service;

import com.bookers.backend.domain.book.dto.BookSaveRequest;
import com.bookers.backend.domain.book.dto.MyBookUpdateRequest;
import com.bookers.backend.domain.book.dto.MyBookResponse;
import com.bookers.backend.domain.book.dto.ShelfStatDto; // [추가] 통계 DTO
import com.bookers.backend.domain.book.entity.Book;
import com.bookers.backend.domain.book.entity.BookGenre; // [추가] 분야 Enum
import com.bookers.backend.domain.book.entity.MyBook;
import com.bookers.backend.domain.book.repository.BookRepository;
import com.bookers.backend.domain.book.repository.MyBookRepository;
import com.bookers.backend.domain.user.entity.User;
import com.bookers.backend.domain.user.repository.UserRepository;
import jakarta.persistence.Tuple; // [추가] 통계용 Tuple
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final MyBookRepository myBookRepository;
    private final UserRepository userRepository;

    // 1. 내 서재에 책 담기 (기능 업그레이드!)
    @Transactional
    public Long addBookToShelf(String email, BookSaveRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        if (myBookRepository.existsByUserIdAndBookIsbn13(user.getId(), request.isbn13())) {
            throw new IllegalArgumentException("이미 서재에 담긴 책입니다.");
        }

        // 책 정보 저장 (없으면 저장, 있으면 가져오기)
        Book book = bookRepository.findByIsbn13(request.isbn13())
                .orElseGet(() -> bookRepository.save(Book.builder()
                        .isbn13(request.isbn13())
                        .title(request.title())
                        .author(request.author())
                        .cover(request.cover())
                        .publisher(request.publisher())
                        .build()));

        // [변경] 내 서재(MyBook)에 저장할 때 분야, 공개범위, 메모도 같이 저장!
        MyBook myBook = MyBook.builder()
                .user(user)
                .book(book)
                .status(request.status())
                .genre(request.genre())         // [추가] 분야
                .visibility(request.visibility()) // [추가] 공개 범위
                .memo(request.memo())           // [추가] 메모
                .build();

        return myBookRepository.save(myBook).getId();
    }

    // 2. 내 서재 조회
    public List<MyBookResponse> getMyBooks(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        return myBookRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(MyBookResponse::from)
                .collect(Collectors.toList());
    }

    // 3. 내 서재 책 수정 (메모, 공개범위 수정 기능 추가)
    @Transactional
    public void updateMyBook(Long myBookId, String email, MyBookUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        MyBook myBook = myBookRepository.findById(myBookId)
                .orElseThrow(() -> new IllegalArgumentException("서재에 담긴 책이 아닙니다."));

        if (!myBook.isOwner(user.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        // [변경] 기존 update 메서드 대신, 새로운 필드들도 업데이트
        // (MyBook 엔티티에 update 메서드를 수정해야 함 - 아래 설명 참조)
        myBook.update(request.status(), request.rating(), request.memo(), request.visibility());
    }

    // 4. 내 서재에서 책 삭제 (기존 동일)
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

    // [신규] 5. 내 서재 통계 보기 (원형 그래프용)
    public List<ShelfStatDto> getMyShelfStats(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        // 1. 튜플로 데이터 가져오기 ( [철학, 5], [경제, 3] ... )
        List<Tuple> results = myBookRepository.getGenreStats(user.getId());

        // 2. 전체 책 권수 계산 (분모)
        long totalCount = results.stream()
                .mapToLong(t -> t.get("count", Long.class))
                .sum();

        // 3. 튜플 -> DTO 변환 (퍼센트 계산 포함)
        return results.stream()
                .map(tuple -> {
                    BookGenre genre = tuple.get("genre", BookGenre.class);
                    Long count = tuple.get("count", Long.class);

                    double percentage = (totalCount == 0) ? 0.0 : (double) count / totalCount * 100;

                    return new ShelfStatDto(genre, count, percentage);
                })
                .collect(Collectors.toList());
    }
}