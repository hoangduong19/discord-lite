package com.discordlite.discord_lite.message.controller;

import com.discordlite.discord_lite.message.dto.ChatMessageRequest;
import com.discordlite.discord_lite.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @MessageMapping("/chat.send")
    public void sendMessage(
            @Payload ChatMessageRequest request,
            @Header("senderId") Long senderId   // 🔥 TEST ONLY
    ) {
        messageService.sendMessage(senderId, request);
    }


}
