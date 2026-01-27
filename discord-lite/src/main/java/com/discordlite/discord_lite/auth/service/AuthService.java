package com.discordlite.discord_lite.auth.service;

import com.discordlite.discord_lite.auth.dto.RegisterRequest;
import com.discordlite.discord_lite.user.entity.User;
import com.discordlite.discord_lite.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        userRepository.save(user);
    }
}
