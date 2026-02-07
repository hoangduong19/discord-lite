package com.discordlite.discord_lite.userServer.entity;

import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.user.entity.User;
import com.discordlite.discord_lite.userServer.compositeKey.UserServerId;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="user_server")
public class UserServer {
    @EmbeddedId
    private UserServerId userServerId;

    @Column (name = "joined_at")
    private Instant joinedAt;

    @ManyToOne
    @MapsId("serverId")
    @JoinColumn(name="server_id")
    private Server server;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name="user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        this.joinedAt = Instant.now();
    }

}
