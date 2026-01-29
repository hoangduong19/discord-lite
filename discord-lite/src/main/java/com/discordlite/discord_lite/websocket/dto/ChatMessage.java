package com.discordlite.discord_lite.websocket.dto;

public record ChatMessage (
    Long channelId,
    String content
){}
