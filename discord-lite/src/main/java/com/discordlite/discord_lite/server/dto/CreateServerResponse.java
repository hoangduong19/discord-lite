package com.discordlite.discord_lite.server.dto;

public record CreateServerResponse(
        Long serverId,
        String serverName,
        String inviteLink
) {}
