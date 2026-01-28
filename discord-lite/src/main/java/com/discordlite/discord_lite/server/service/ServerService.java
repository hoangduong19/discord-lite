package com.discordlite.discord_lite.server.service;

import com.discordlite.discord_lite.inviteCode.entity.InviteCode;
import com.discordlite.discord_lite.inviteCode.repository.InviteCodeRepository;
import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ServerService {
    private static final String INVITE_BASE_URL = "https://discord-lite.gg/";
    private final ServerRepository serverRepository;
    private final InviteCodeRepository inviteCodeRepository;

    public Server createServer(String serverName) {

        Server server = new Server();
        server.setServerName(serverName);

        Server savedServer = serverRepository.save(server);

        InviteCode inviteCode = new InviteCode();
        inviteCode.setInviteCodeLink(generateInviteLink());
        inviteCode.setExpiredAt(LocalDateTime.now().plusDays(30));
        inviteCode.setServer(server);
        server.setInviteCode(inviteCode);

        serverRepository.save(server);
        return savedServer;
    }

    private String generateInviteCode() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);
    }

    private String generateInviteLink() {
        return INVITE_BASE_URL + generateInviteCode();
    }
}
