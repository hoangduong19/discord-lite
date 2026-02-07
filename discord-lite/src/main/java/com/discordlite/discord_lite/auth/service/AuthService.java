package com.discordlite.discord_lite.auth.service;

import com.discordlite.discord_lite.auth.dto.LoginRequest;
import com.discordlite.discord_lite.auth.dto.LoginResponse;
import com.discordlite.discord_lite.auth.dto.RegisterRequest;
import com.discordlite.discord_lite.auth.dto.VerifyEmailRequest;
import com.discordlite.discord_lite.auth.enums.VerificationPurpose;
import com.discordlite.discord_lite.exception.ErrorCode;
import com.discordlite.discord_lite.exception.newException.ApiException;
import com.discordlite.discord_lite.security.jwt.JwtService;
import com.discordlite.discord_lite.user.entity.User;
import com.discordlite.discord_lite.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailVerificationService emailVerificationService;

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exist");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exist");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setDisplayName(request.displayName());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmailVerified(false);

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_USERNAME));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ApiException(ErrorCode.INVALID_PASSWORD);
        }

        if (!user.isEnabled()) {
            throw new ApiException(ErrorCode.USER_DISABLED);
        }

        if (!user.isEmailVerified()) {
            throw new ApiException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        String token = jwtService.generateToken(
                user.getUserId(),
                user.getUsername()
        );
        return new LoginResponse(token);
    }

    public void verifyEmail (VerifyEmailRequest request) {
        emailVerificationService.verifyOtp(request.username(), request.code(), VerificationPurpose.EMAIL_VERIFY);
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmailVerified(true);

        userRepository.save(user);
    }
}
