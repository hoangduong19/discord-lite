package com.discordlite.discord_lite.channel.entity;

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

    @ManyToOne
    @JoinColumn(name = "server_id", nullable = false)
    private Server server;
}
