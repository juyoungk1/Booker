package com.bookers.backend.global.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long expiration;

    // 생성자에서 properties의 값을 가져와서 키를 세팅합니다.
    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.expiration}") long expiration) {
        //byte[] keyBytes = Decoders.BASE64.decode(secretKey); // 혹은 getBytes() 사용 가능 (간단하게)
        // 위 secretKey가 Base64 인코딩이 안 된 평문이라면 아래처럼 쓰세요:
        // this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        //this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    // 1. 토큰 생성 (이메일을 담음)
    public String createToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(email) // 토큰의 주인(Subject) = 이메일
                .setIssuedAt(now) // 발행 시간
                .setExpiration(validity) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘
                .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 만료되었거나 조작되었을 경우 false 반환
            return false;
        }
    }

    // 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}