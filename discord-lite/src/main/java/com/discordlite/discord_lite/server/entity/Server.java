package com.discordlite.discord_lite.server.entity;

import com.discordlite.discord_lite.channel.entity.Channel;
import com.discordlite.discord_lite.inviteCode.entity.InviteCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "servers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "server_id", nullable = false, updatable = false)
    private Long serverId;

    @Column(name = "server_name", nullable = false, length = 255)
    private String serverName;

    // 1 server có nhiều channel
    @OneToMany(mappedBy = "server")
    private List<Channel> channels;

    // 1 server chi co 1 invite code
    @OneToOne(mappedBy = "server", cascade = CascadeType.ALL)
    private InviteCode inviteCode;
}
