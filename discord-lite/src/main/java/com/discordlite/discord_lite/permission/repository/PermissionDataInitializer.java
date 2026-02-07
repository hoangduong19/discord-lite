package com.discordlite.discord_lite.permission.repository;

import com.discordlite.discord_lite.permission.constant.PermissionConstant;
import com.discordlite.discord_lite.permission.entity.Permission;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PermissionDataInitializer {
    @Bean
    CommandLineRunner initPermissions(PermissionRepository repo) {
        return args -> {

            insertIfNotExists(repo,
                    PermissionConstant.VIEW_SERVER, "View server");
            insertIfNotExists(repo,
                    PermissionConstant.MANAGE_SERVER, "Manage server");
            insertIfNotExists(repo,
                    PermissionConstant.CREATE_INVITE, "Create invite");
            insertIfNotExists(repo,
                    PermissionConstant.MANAGE_ROLES, "Manage roles");
            insertIfNotExists(repo,
                    PermissionConstant.KICK_MEMBER, "Kick member");

            insertIfNotExists(repo,
                    PermissionConstant.VIEW_CHANNEL, "View channel");
            insertIfNotExists(repo,
                    PermissionConstant.CREATE_CHANNEL, "Create channel");
            insertIfNotExists(repo,
                    PermissionConstant.DELETE_CHANNEL, "Delete channel");
            insertIfNotExists(repo,
                    PermissionConstant.MANAGE_CHANNEL, "Manage channel");

            insertIfNotExists(repo,
                    PermissionConstant.SEND_MESSAGE, "Send message");
            insertIfNotExists(repo,
                    PermissionConstant.READ_MESSAGE, "Read message history");
            insertIfNotExists(repo,
                    PermissionConstant.DELETE_MESSAGE, "Delete message");

            insertIfNotExists(repo,
                    PermissionConstant.ADMINISTRATOR, "Administrator (all permissions)");
        };
    }

    private void insertIfNotExists(
            PermissionRepository repo,
            String id,
            String name
    ) {
        if (!repo.existsById(id)) {
            repo.save(new Permission(id, name));
        }
    }
}
