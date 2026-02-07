package com.discordlite.discord_lite.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1️⃣ Lấy Authorization header
        String authHeader = request.getHeader("Authorization");

        // Không có token → cho qua
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2️⃣ Cắt lấy token
        String token = authHeader.substring(7);// cut "Bearer " part

        // 3️⃣ Kiểm tra token hết hạn
        if (jwtService.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4️⃣ Lấy userId từ token
        Long userId = jwtService.extractUserId(token);

        // 5️⃣ Tạo Authentication
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userId,                    // principal
                        null,
                        Collections.emptyList()    // chưa dùng role
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // 6️⃣ Gắn vào SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 7️⃣ Cho request đi tiếp
        filterChain.doFilter(request, response);
    }
}