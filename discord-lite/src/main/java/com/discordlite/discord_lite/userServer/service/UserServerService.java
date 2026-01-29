package com.discordlite.discord_lite.userServer.service;

import com.discordlite.discord_lite.inviteCode.entity.InviteCode;
import com.discordlite.discord_lite.inviteCode.repository.InviteCodeRepository;
import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.user.entity.User;
import com.discordlite.discord_lite.user.repository.UserRepository;
import com.discordlite.discord_lite.userServer.compositeKey.UserServerId;
import com.discordlite.discord_lite.userServer.dto.JoinServerResponse;
import com.discordlite.discord_lite.userServer.entity.UserServer;
import com.discordlite.discord_lite.userServer.repository.UserServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserServerService {
    private final UserServerRepository userServerRepository;
    private final InviteCodeRepository inviteCodeRepository;
    private final UserRepository userRepository;

    public JoinServerResponse joinServer(String username, String inviteCode) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InviteCode code = inviteCodeRepository.findByInviteCodeLink(inviteCode)
                .orElseThrow(() -> new RuntimeException("Invalid invite code"));

        if (userServerRepository.existsByUserAndServer(user, code.getServer())) {
            throw new RuntimeException("User already joined~");
        }
        Server server = code.getServer();
        UserServer userServer = new UserServer();

        userServer.setUserServerId(new UserServerId(user.getUserId(), server.getServerId()));
        userServer.setUser(user);
        userServer.setServer(server);
        userServer.setJoinedAt(Instant.now());

        userServerRepository.save(userServer);
        return new JoinServerResponse(code.getServer().getServerId(),
                server.getServerName(), userServer.getJoinedAt());
    }
}
