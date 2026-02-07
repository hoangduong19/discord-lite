package com.discordlite.discord_lite.channel.controller;

import com.discordlite.discord_lite.channel.dto.CreateChannelRequest;
import com.discordlite.discord_lite.channel.dto.CreateChannelResponse;
import com.discordlite.discord_lite.channel.entity.Channel;
import com.discordlite.discord_lite.channel.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/servers/{serverId}/channels")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping("/create")
    public ResponseEntity<CreateChannelResponse> createChannel(
            @PathVariable Long serverId, @RequestBody CreateChannelRequest request) {
        Channel channel = channelService.createChannel(serverId, request.channelName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateChannelResponse(
                        channel.getChannelId(),
                        channel.getChannelName()
                ));
    }


    @GetMapping
    public ResponseEntity<List<CreateChannelResponse>> listChannels(
            @PathVariable Long serverId) {

        return ResponseEntity.ok(
                channelService.listChannels(serverId)
        );
    }
}
