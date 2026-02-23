package com.discordlite.discord_lite.user.controller;

import com.discordlite.discord_lite.security.CurrentUserService;
import com.discordlite.discord_lite.user.dto.UserResponse;
import com.discordlite.discord_lite.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/my-information")
    public UserResponse getMyInformation (CurrentUserService currentUserService) {
        Long userId = currentUserService.getCurrentUserId();
        return userService.getInformationById(userId);
    }
}
