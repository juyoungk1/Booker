package com.bookers.backend.domain.user.controller;

import com.bookers.backend.domain.user.dto.LoginRequest;
import com.bookers.backend.domain.user.dto.LoginResponse;
import com.bookers.backend.domain.user.dto.SignupRequest;
import com.bookers.backend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }



    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        System.out.println("1. 로그인 요청 들어옴: " + request.email()); // 로그 추가
        String token = userService.login(request);
        System.out.println("4. 토큰 발급 완료: " + token); // 로그 추가
        return ResponseEntity.ok(new LoginResponse(token));
    }

}