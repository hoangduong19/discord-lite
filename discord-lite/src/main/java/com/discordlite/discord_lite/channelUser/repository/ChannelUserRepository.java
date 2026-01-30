package com.discordlite.discord_lite.channelUser.repository;

import com.discordlite.discord_lite.channelUser.compositeKey.ChannelUserId;
import com.discordlite.discord_lite.channelUser.entity.ChannelUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelUserRepository extends JpaRepository<ChannelUser, ChannelUserId> {

}
