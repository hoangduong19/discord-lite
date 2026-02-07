package com.discordlite.discord_lite.security;

import com.discordlite.discord_lite.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void testGenerateToken_andExtractUserId() {
        // given
        Long userId = 1L;
        String username = "duong";

        // when
        String token = jwtService.generateToken(userId, username);
        Long extractedUserId = jwtService.extractUserId(token);

        // then
        assertNotNull(token, "Token không được null");
        assertEquals(userId, extractedUserId,
                "UserId trích xuất phải giống userId ban đầu");
    }

    @Test
    void testTokenNotExpiredImmediately() {
        // given
        String token = jwtService.generateToken(1L, "duong");

        // when
        boolean expired = jwtService.isExpired(token);

        // then
        assertFalse(expired, "Token mới tạo không được hết hạn");
    }

    @Test
    void testDifferentTokensForDifferentUsers() {
        String token1 = jwtService.generateToken(1L, "duong");
        String token2 = jwtService.generateToken(2L, "admin");

        assertNotEquals(token1, token2,
                "Token của hai user khác nhau phải khác nhau");
    }
}