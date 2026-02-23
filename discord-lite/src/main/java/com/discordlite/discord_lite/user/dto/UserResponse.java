package com.discordlite.discord_lite.user.dto;

import java.time.Instant;

public record UserResponse (
        String avatar,
        Instant createdAt,
        String email,
        String displayName
) {}
