package com.discordlite.discord_lite.userServer.dto;

import jakarta.validation.constraints.NotBlank;

public record JoinServerRequest (
        @NotBlank
        String inviteCode,
        @NotBlank
        String username
)
{}
