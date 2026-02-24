package com.discordlite.discord_lite.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    @Value("${JWT_SECRET_KEY}")
    private String SECRET_KEY;

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60;

    //Tạo khóa bí mật
    private Key getKey() {
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    // 1. Tạo token
    public String generateToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )                                   // hết hạn sau 1h
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //Giải mã token & lấy dữ liệu
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 2. Lấy username từ token
    public Long extractUserId(String token) {
        return Long.parseLong(getClaims(token).getSubject());
    }

    // 3. Kiểm tra token hết hạn chưa
    public boolean isExpired(String token) {
        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

}
