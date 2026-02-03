package com.bookers.backend.global.config;

import com.bookers.backend.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // 이 import가 꼭 필요합니다!
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil; // Lombok이 생성자로 주입해줍니다.

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // [1] 열어줄 곳들을 먼저 다 나열합니다.
                        .requestMatchers("/api/auth/**").permitAll() // 로그인, 회원가입
                        .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll() // 도서 검색
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll() // 게시글 조회
                        .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll() // 댓글 조회
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // [2] 나머지는 인증 필요 (무조건 맨 마지막!)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}