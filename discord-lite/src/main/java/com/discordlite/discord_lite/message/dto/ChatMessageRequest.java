package com.discordlite.discord_lite.message.dto;

import com.discordlite.discord_lite.message.enums.ChatTargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatMessageRequest(
        @NotNull ChatTargetType type,
        @NotNull Long targetId,     // channelId / userId
        @NotBlank String content
) {}