package com.discordlite.discord_lite.auth.dto;

public record LoginResponse(
        Long userId,
        String username
) {}