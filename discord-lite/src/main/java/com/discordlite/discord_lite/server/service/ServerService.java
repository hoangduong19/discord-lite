package com.discordlite.discord_lite.server.service;

import com.discordlite.discord_lite.inviteCode.entity.InviteCode;
import com.discordlite.discord_lite.inviteCode.repository.InviteCodeRepository;
import com.discordlite.discord_lite.permission.constant.PermissionConstant;
import com.discordlite.discord_lite.permission.repository.PermissionRepository;
import com.discordlite.discord_lite.role.entity.Role;
import com.discordlite.discord_lite.role.repository.RoleRepository;
import com.discordlite.discord_lite.security.CurrentUserService;
import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.server.repository.ServerRepository;
import com.discordlite.discord_lite.userRoleServer.compositeKey.UserRoleServerId;
import com.discordlite.discord_lite.userRoleServer.entity.UserRoleServer;
import com.discordlite.discord_lite.userRoleServer.repository.UserRoleServerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ServerService {
    private static final String INVITE_BASE_URL = "https://discord-lite.gg/";
    private final ServerRepository serverRepository;
    private final InviteCodeRepository inviteCodeRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRoleServerRepository userRoleServerRepository;

    private final CurrentUserService currentUserService;

    @Transactional
    public Server createServer(String serverName) {
        Long userId = currentUserService.getCurrentUserId();

        Server server = new Server();
        server.setServerName(serverName);

        Server savedServer = serverRepository.save(server);

        InviteCode inviteCode = new InviteCode();
        inviteCode.setInviteCodeLink(generateInviteLink());
        inviteCode.setExpiredAt(LocalDateTime.now().plusDays(30));
        inviteCode.setServer(server);
        server.setInviteCode(inviteCode);

        serverRepository.save(server);

        Role ownerRole = new Role();
        ownerRole.setRoleName("OWNER");
        ownerRole.setServerId(savedServer.getServerId());
        ownerRole.setPermissions(Set.of(
                permissionRepository.getReferenceById(PermissionConstant.ADMINISTRATOR)
        ));
        roleRepository.save(ownerRole);


        // MEMBER
        Role memberRole = new Role();
        memberRole.setRoleName("MEMBER");
        memberRole.setServerId(savedServer.getServerId());
        memberRole.setPermissions(Set.of(
                permissionRepository.getReferenceById(PermissionConstant.VIEW_SERVER),
                permissionRepository.getReferenceById(PermissionConstant.VIEW_CHANNEL),
                permissionRepository.getReferenceById(PermissionConstant.READ_MESSAGE),
                permissionRepository.getReferenceById(PermissionConstant.SEND_MESSAGE)
        ));
        roleRepository.save(memberRole);


        userRoleServerRepository.save(
                new UserRoleServer(new UserRoleServerId(
                        userId,
                        ownerRole.getRoleId(),
                        savedServer.getServerId()))
        );
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
