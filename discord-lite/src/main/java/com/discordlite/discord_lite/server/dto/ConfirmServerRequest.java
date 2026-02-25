package com.discordlite.discord_lite.server.dto;

public record ConfirmServerRequest (
        Long serverId,
        String key
) {
}
