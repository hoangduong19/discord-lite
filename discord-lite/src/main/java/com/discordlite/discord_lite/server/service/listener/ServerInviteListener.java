package com.discordlite.discord_lite.server.service.listener;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.discordlite.discord_lite.inviteCode.entity.InviteCode;
import com.discordlite.discord_lite.inviteCode.repository.InviteCodeRepository;
import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.server.service.publisher.ServerCreatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ServerInviteListener {
    private final InviteCodeRepository inviteCodeRepository;
    private static final String INVITE_BASE_URL = "https://discord-lite.gg/";

    @EventListener
    public void handleInviteCode(ServerCreatedEvent event) {
        Server server = event.getServer();

        InviteCode inviteCode = new InviteCode();
        inviteCode.setInviteCodeLink(INVITE_BASE_URL + UUID.randomUUID().toString().substring(0, 8));
        inviteCode.setExpiredAt(LocalDateTime.now().plusDays(30));
        inviteCode.setServer(server);
        server.setInviteCode(inviteCode);
        inviteCodeRepository.save(inviteCode);

    }
}