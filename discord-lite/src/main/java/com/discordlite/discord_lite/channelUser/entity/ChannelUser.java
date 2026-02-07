package com.discordlite.discord_lite.channelUser.entity;

import com.discordlite.discord_lite.channel.entity.Channel;
import com.discordlite.discord_lite.channelUser.compositeKey.ChannelUserId;
import com.discordlite.discord_lite.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "channel_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChannelUser {
    @EmbeddedId
    private ChannelUserId channelUserId;

    @ManyToOne
    @MapsId("channelId")
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
