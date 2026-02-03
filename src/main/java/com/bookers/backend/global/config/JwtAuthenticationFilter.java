package com.bookers.backend.global.config;

import com.bookers.backend.domain.user.entity.Role; // Role Enum 필요
import com.bookers.backend.global.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("🔍 [1] 필터 진입: " + request.getRequestURI()); // 로그 추가

        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("🔍 [2] 헤더 값 확인: " + authorizationHeader); // 로그 추가

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            // 토큰 유효성 검사 결과 로그 찍기
            boolean isValid = jwtUtil.validateToken(token);
            System.out.println("🔍 [3] 토큰 유효성 검사 결과: " + isValid); // 로그 추가

            if (isValid) {
                String email = jwtUtil.getEmailFromToken(token);
                System.out.println("🔍 [4] 인증된 이메일: " + email); // 로그 추가

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority(Role.USER.name()))
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("🔍 [5] SecurityContext 저장 완료"); // 로그 추가
            } else {
                System.out.println("🚨 [3-FAIL] 토큰이 유효하지 않음 (키 불일치 또는 만료)");
            }
        } else {
            System.out.println("🚨 [2-FAIL] 헤더가 없거나 Bearer로 시작하지 않음");
        }

        filterChain.doFilter(request, response);
    }
}