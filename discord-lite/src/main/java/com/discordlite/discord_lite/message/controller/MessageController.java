package com.discordlite.discord_lite.message.controller;

import com.discordlite.discord_lite.message.dto.ChatMessageRequest;
import com.discordlite.discord_lite.message.dto.ChatMessageResponse;
import com.discordlite.discord_lite.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("channels")
public class MessageController {
    private final MessageService messageService;

    @MessageMapping("/chat.send")
    public void sendMessage(
            @Payload ChatMessageRequest request,
            @Header("senderId") Long senderId   // 🔥 TEST ONLY
    ) {
        messageService.sendMessage(senderId, request);
    }

    @GetMapping("/{channelId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> loadAllMessages (@PathVariable Long channelId) {
        return ResponseEntity.ok(messageService.loadAllMessages(channelId));
    }

}
