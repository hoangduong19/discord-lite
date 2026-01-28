package com.discordlite.discord_lite.channel.service;

import com.discordlite.discord_lite.channel.dto.CreateChannelResponse;
import com.discordlite.discord_lite.channel.entity.Channel;
import com.discordlite.discord_lite.channel.repository.ChannelRepository;
import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final ServerRepository serverRepository;

    public Channel createChannel (Long serverId, String channelName) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Server not found"));
        Channel channel = new Channel();
        channel.setChannelName(channelName);
        channel.setServer(server);

        channelRepository.save(channel);
        return channel;
    }

    public List<CreateChannelResponse> listChannels(Long serverId) {
        return channelRepository.findByServer_ServerId(serverId).stream()
                .map(channel -> new CreateChannelResponse(
                        channel.getChannelId(),
                        channel.getChannelName()
                ))
                .toList();
    }
}
