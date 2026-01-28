package com.discordlite.discord_lite.auth.controller;

import com.discordlite.discord_lite.auth.dto.LoginRequest;
import com.discordlite.discord_lite.auth.dto.LoginResponse;
import com.discordlite.discord_lite.auth.dto.RegisterRequest;
import com.discordlite.discord_lite.auth.dto.RegisterResponse;
import com.discordlite.discord_lite.auth.service.AuthService;
import com.discordlite.discord_lite.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        User user = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RegisterResponse(
                        user.getUserId(),
                        user.getUsername()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        User user = authService.login(request);

        return ResponseEntity.ok(
                new LoginResponse(
                        user.getUserId(),
                        user.getUsername()
                )
        );
    }

}
