package com.discordlite.discord_lite.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    private static final String SECRET_KEY =
            "I-am-god-of-thunder";

    // 1. Tạo token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 60 * 60 * 1000)
                )                                   // hết hạn sau 1h
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. Lấy username từ token
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // 3. Kiểm tra token hết hạn chưa
    public boolean isExpired(String token) {
        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

    //Giải mã token & lấy dữ liệu
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Tạo khóa bí mật
    private Key getKey() {
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }
}
