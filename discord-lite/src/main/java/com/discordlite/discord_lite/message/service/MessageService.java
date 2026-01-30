package com.discordlite.discord_lite.message.service;

import com.discordlite.discord_lite.channel.entity.Channel;
import com.discordlite.discord_lite.channel.repository.ChannelRepository;
import com.discordlite.discord_lite.channel.service.ChannelService;
import com.discordlite.discord_lite.message.dto.ChatMessageRequest;
import com.discordlite.discord_lite.message.entity.Message;
import com.discordlite.discord_lite.message.enums.ChatTargetType;
import com.discordlite.discord_lite.message.repository.MessageRepository;
import com.discordlite.discord_lite.user.entity.User;
import com.discordlite.discord_lite.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    private final ChannelService channelService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    public void sendMessage(Long userId, ChatMessageRequest request) {
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Channel channel = resolveChannel(sender, request);

        Message message = new Message();
        message.setUser(sender);
        message.setChannel(channel);
        message.setContent(request.content());
        message.setCreatedAt(Instant.now());

        Message saved = messageRepository.save(message);

        simpMessagingTemplate.convertAndSend("/topic/channel/" + channel.getChannelId(), saved);
    }

    private Channel resolveChannel(User sender, ChatMessageRequest request) {
        return switch (request.type()) {

            case CHANNEL -> channelRepository.findById(request.targetId())
                    .orElseThrow(() -> new RuntimeException("Channel not found"));

            case USER -> {
                User receiver = userRepository.findById(request.targetId())
                        .orElseThrow(() -> new RuntimeException("Receiver not found"));
                yield channelService.getOrCreateDirectChannel(sender, receiver); //tra ve channel thay vi return thi dung yield
            }

        };
    }

}
