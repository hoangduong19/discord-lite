package com.discordlite.discord_lite.channel.repository;

import com.discordlite.discord_lite.channel.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findByServer_ServerId(Long serverId);
}