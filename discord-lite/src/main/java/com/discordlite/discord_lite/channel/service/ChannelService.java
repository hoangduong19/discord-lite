package com.discordlite.discord_lite.channel.service;

import com.discordlite.discord_lite.channel.dto.CreateChannelResponse;
import com.discordlite.discord_lite.channel.entity.Channel;
import com.discordlite.discord_lite.channel.enums.ChannelType;
import com.discordlite.discord_lite.channel.repository.ChannelRepository;
import com.discordlite.discord_lite.channelUser.compositeKey.ChannelUserId;
import com.discordlite.discord_lite.channelUser.entity.ChannelUser;
import com.discordlite.discord_lite.channelUser.repository.ChannelUserRepository;
import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.server.repository.ServerRepository;
import com.discordlite.discord_lite.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final ServerRepository serverRepository;
    private final ChannelUserRepository channelUserRepository;

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

    @Transactional
    public Channel getOrCreateDirectChannel(User a, User b) {

        //Tìm DM đã tồn tại
        Optional<Channel> existing =
                channelRepository.findDirectChannelBetween(
                        a.getUserId(),
                        b.getUserId()
                );

        if (existing.isPresent()) {
            return existing.get();
        }

        //Chưa có → tạo mới
        Channel channel = new Channel();
        channel.setType(ChannelType.DIRECT);
        channel.setChannelName("DM");
        channel.setServer(null);

        Channel savedChannel = channelRepository.save(channel);

        //Add member A
        channelUserRepository.save(
                new ChannelUser(
                        new ChannelUserId(
                                savedChannel.getChannelId(),
                                a.getUserId()
                        ),
                        savedChannel,
                        a
                )
        );

        // Add member B
        channelUserRepository.save(
                new ChannelUser(
                        new ChannelUserId(
                                savedChannel.getChannelId(),
                                b.getUserId()
                        ),
                        savedChannel,
                        b
                )
        );

        return savedChannel;
    }
}
