package com.discordlite.discord_lite.permission.service;

import com.discordlite.discord_lite.permission.constant.PermissionConstant;
import com.discordlite.discord_lite.role.entity.Role;
import com.discordlite.discord_lite.userRoleServer.repository.UserRoleServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final UserRoleServerRepository userRoleServerRepository;
    public void checkPermission(Long userId, Long serverId, String requiredPermission) {
        List<Role> roles = userRoleServerRepository.findRolesOfUserInServer(userId, serverId);
        if (roles.isEmpty()) {
            throw new AccessDeniedException("User has no role");
        }

        boolean isAdmin = roles.stream() // 1 lon role -> nhieu pẻmission
                .flatMap(r-> r.getPermissions().stream())
                .anyMatch(p -> p.getPermissionId().equals(PermissionConstant.ADMINISTRATOR));
        if (isAdmin) return;


        boolean allowed = roles.stream()
                .flatMap(r -> r.getPermissions().stream())
                .anyMatch(p -> p.getPermissionId().equals(requiredPermission));
        if (!allowed) {
            throw new AccessDeniedException("Permission denied");
        }
    }
}
