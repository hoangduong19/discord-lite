package com.discordlite.discord_lite.auth.dto;

public record RegisterResponse(
        Long userId,
        String username
) {}