package com.discordlite.discord_lite.auth.controller;

import com.discordlite.discord_lite.auth.dto.*;
import com.discordlite.discord_lite.auth.enums.VerificationPurpose;
import com.discordlite.discord_lite.auth.service.AuthService;
import com.discordlite.discord_lite.auth.service.EmailVerificationService;
import com.discordlite.discord_lite.security.jwt.JwtService;
import com.discordlite.discord_lite.user.entity.User;
import com.discordlite.discord_lite.user.service.UserService;
import com.discordlite.discord_lite.userServer.entity.UserServer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final EmailVerificationService emailVerificationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "Verification code sent to username"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request);
        return ResponseEntity.ok(Map.of("message", "Email verified"));
    }

    @PostMapping("send-verification-email")
    public ResponseEntity<Void> sendVerificationEmail (
            @RequestBody SendVerificationEmailRequest request
    ) {
        User user = userService.findByUsername(request.username());

        emailVerificationService.sendOtp(
                user.getEmail(),
                request.purpose()
        );
        return ResponseEntity.ok().build();
    }

}
