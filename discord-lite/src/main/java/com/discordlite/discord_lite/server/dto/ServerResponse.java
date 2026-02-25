package com.discordlite.discord_lite.server.dto;

public record ServerResponse(
        Long id,
        String serverName,
        String avatarUrl
) {
}
