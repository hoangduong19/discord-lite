package com.discordlite.discord_lite.channel.entity;

import com.discordlite.discord_lite.channel.enums.ChannelType;
import com.discordlite.discord_lite.server.entity.Server;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "channels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id", nullable = false, updatable = false)
    private Long channelId;

    @Column(name = "channel_name", nullable = false, length = 255)
    private String channelName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChannelType type;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private Server server;
}
