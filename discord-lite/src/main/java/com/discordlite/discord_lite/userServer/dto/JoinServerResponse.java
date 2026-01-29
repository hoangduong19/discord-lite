package com.discordlite.discord_lite.userServer.dto;

import java.time.Instant;

public record JoinServerResponse (
        Long serverId,
        String serverName,
        Instant joinedAt
)
{}
