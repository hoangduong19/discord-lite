package com.discordlite.discord_lite.server.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateServerRequest (
        @NotBlank
        String serverName
)
{}
