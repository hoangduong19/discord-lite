package com.discordlite.discord_lite.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
        @NotBlank
        String username,

        @NotBlank
        String password

)
{}
