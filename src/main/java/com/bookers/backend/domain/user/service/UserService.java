package com.bookers.backend.domain.user.service;

import com.bookers.backend.domain.user.dto.LoginRequest;
import com.bookers.backend.domain.user.dto.SignupRequest;
import com.bookers.backend.domain.user.entity.Role;
import com.bookers.backend.domain.user.entity.User;
import com.bookers.backend.domain.user.repository.UserRepository;
import com.bookers.backend.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용 (성능 최적화)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional // 쓰기 허용
    public Long signup(SignupRequest request) {
        // 1. 중복 검증
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByNickname(request.nickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());

        // 3. 저장
        User user = User.builder()
                .email(request.email())
                .password(encodedPassword)
                .nickname(request.nickname())
                .role(Role.USER)
                .build();

        return userRepository.save(user).getId();
    }

    public String login(LoginRequest request) {
        System.out.println("2. 서비스 진입");
        // 1. 이메일 검증
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        System.out.println("3. 유저 찾음: " + user.getEmail()); // 로그 추가

        // 2. 비밀번호 검증 (입력비번 vs DB비번)
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 토큰 생성 및 반환
        return jwtUtil.createToken(user.getEmail());
    }




}