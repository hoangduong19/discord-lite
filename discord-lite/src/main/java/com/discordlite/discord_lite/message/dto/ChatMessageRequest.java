package com.discordlite.discord_lite.message.dto;

import com.discordlite.discord_lite.message.enums.ChatTargetType;

public record ChatMessageRequest(
        ChatTargetType type,
        Long targetId,     // channelId / userId
        String content
) {}