package com.discordlite.discord_lite.inviteCode.entity;

import com.discordlite.discord_lite.server.entity.Server;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "invite_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InviteCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invite_code_id", updatable = false, nullable = false)
    private Long inviteCodeId;

    @Column(name = "invite_code_link", nullable = false, length = 255, unique = true)
    private String inviteCodeLink;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @OneToOne
    @JoinColumn(name = "server_id", nullable = false, unique = true)
    private Server server;
}
