package com.discordlite.discord_lite.message.dto;

import com.discordlite.discord_lite.message.entity.Message;
import lombok.Getter;

import java.time.Instant;

public record ChatMessageResponse (
       Long messageId,
       Long userId,
       String username,
       String content,
       Instant createdAt
) {
    public static ChatMessageResponse from(Message message) {
        return new ChatMessageResponse(
                message.getMessageId(),
                message.getUser().getUserId(),
                message.getUser().getUsername(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
