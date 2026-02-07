package com.discordlite.discord_lite.auth.dto;

public record VerifyEmailRequest (
        String username,
        String code
)
{}
