package com.discordlite.discord_lite.user.service;

import com.discordlite.discord_lite.user.dto.UserResponse;
import com.discordlite.discord_lite.user.entity.User;
import com.discordlite.discord_lite.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse getInformationById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found"));
    }

    private UserResponse mapToResponse(User user) {
        String avatar = Optional.ofNullable(user.getAvatar())
                .orElse("http://localhost:8080/images/2.png");

        return new UserResponse(
                avatar,
                user.getCreatedAt(),
                user.getEmail(),
                user.getDisplayName()
        );
    }
}
