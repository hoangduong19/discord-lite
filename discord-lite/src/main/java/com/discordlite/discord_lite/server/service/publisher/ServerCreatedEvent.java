package com.discordlite.discord_lite.server.service.publisher;

import com.discordlite.discord_lite.server.entity.Server;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServerCreatedEvent {
    private final Server server;
    private final Long userId;
}
