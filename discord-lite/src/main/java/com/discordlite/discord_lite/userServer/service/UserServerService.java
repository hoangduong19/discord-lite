package com.discordlite.discord_lite.userServer.service;

import com.discordlite.discord_lite.inviteCode.entity.InviteCode;
import com.discordlite.discord_lite.inviteCode.repository.InviteCodeRepository;
import com.discordlite.discord_lite.role.entity.Role;
import com.discordlite.discord_lite.role.repository.RoleRepository;
import com.discordlite.discord_lite.security.CurrentUserService;
import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.user.entity.User;
import com.discordlite.discord_lite.user.repository.UserRepository;
import com.discordlite.discord_lite.userRoleServer.compositeKey.UserRoleServerId;
import com.discordlite.discord_lite.userRoleServer.entity.UserRoleServer;
import com.discordlite.discord_lite.userRoleServer.repository.UserRoleServerRepository;
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
    private final RoleRepository roleRepository;
    private final UserRoleServerRepository userRoleServerRepository;

    private final CurrentUserService currentUserService;

    public JoinServerResponse joinServer(String inviteCode) {
        Long userId = currentUserService.getCurrentUserId();
        User user = userRepository.findById(userId)
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

        Role memberRole = roleRepository
                .findByServerIdAndRoleName(
                        server.getServerId(),
                        "MEMBER"
                )
                .orElseThrow(() ->
                        new RuntimeException("MEMBER role not found")
                );

        userRoleServerRepository.save(
                new UserRoleServer(
                        new UserRoleServerId(
                                userId,
                                memberRole.getRoleId(),
                                server.getServerId()
                        )
                )
        );
        return new JoinServerResponse(code.getServer().getServerId(),
                server.getServerName(), userServer.getJoinedAt());
    }
}
