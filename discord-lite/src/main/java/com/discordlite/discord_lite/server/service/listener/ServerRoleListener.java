package com.discordlite.discord_lite.server.service.listener;

import java.time.Instant;
import java.util.Set;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.discordlite.discord_lite.permission.constant.PermissionConstant;
import com.discordlite.discord_lite.permission.repository.PermissionRepository;
import com.discordlite.discord_lite.role.entity.Role;
import com.discordlite.discord_lite.role.repository.RoleRepository;
import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.server.service.publisher.ServerCreatedEvent;
import com.discordlite.discord_lite.user.repository.UserRepository;
import com.discordlite.discord_lite.userRoleServer.compositeKey.UserRoleServerId;
import com.discordlite.discord_lite.userRoleServer.entity.UserRoleServer;
import com.discordlite.discord_lite.userRoleServer.repository.UserRoleServerRepository;
import com.discordlite.discord_lite.userServer.compositeKey.UserServerId;
import com.discordlite.discord_lite.userServer.entity.UserServer;
import com.discordlite.discord_lite.userServer.repository.UserServerRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ServerRoleListener {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRoleServerRepository userRoleServerRepository;
    private final UserServerRepository userServerRepository;
    private final UserRepository userRepository;

    @EventListener // Spring sẽ tự gọi hàm này khi có ServerCreatedEvent
    public void handleRoleAndMapping(ServerCreatedEvent event) {
        Server server = event.getServer();
        Long userId = event.getUserId();

        // 1. Map User với Server
        UserServer userServer = new UserServer();
        userServer.setUserServerId(new UserServerId(userId, server.getServerId()));
        userServer.setJoinedAt(Instant.now());
        userServer.setServer(server);
        userServer.setUser(userRepository.getReferenceById(userId));
        userServerRepository.save(userServer);

        // 2. Tạo OWNER Role
        Role ownerRole = new Role();
        ownerRole.setRoleName("OWNER");
        ownerRole.setServerId(server.getServerId());
        ownerRole.setPermissions(Set.of(
                permissionRepository.getReferenceById(PermissionConstant.ADMINISTRATOR)
        ));
        Role savedOwnerRole = roleRepository.save(ownerRole);

        // 3. Tạo MEMBER Role
        Role memberRole = new Role();
        memberRole.setRoleName("MEMBER");
        memberRole.setServerId(server.getServerId());
        memberRole.setPermissions(Set.of(
                permissionRepository.getReferenceById(PermissionConstant.VIEW_SERVER),
                permissionRepository.getReferenceById(PermissionConstant.VIEW_CHANNEL),
                permissionRepository.getReferenceById(PermissionConstant.READ_MESSAGE),
                permissionRepository.getReferenceById(PermissionConstant.SEND_MESSAGE)
        ));
        roleRepository.save(memberRole);

        // 4. Gán quyền OWNER cho người tạo
        userRoleServerRepository.save(new UserRoleServer(
                new UserRoleServerId(userId, savedOwnerRole.getRoleId(), server.getServerId()))
        );
    }
}