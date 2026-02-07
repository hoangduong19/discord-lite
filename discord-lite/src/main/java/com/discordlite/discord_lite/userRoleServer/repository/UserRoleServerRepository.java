package com.discordlite.discord_lite.userRoleServer.repository;

import com.discordlite.discord_lite.role.entity.Role;
import com.discordlite.discord_lite.userRoleServer.compositeKey.UserRoleServerId;
import com.discordlite.discord_lite.userRoleServer.entity.UserRoleServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleServerRepository extends JpaRepository<UserRoleServer, UserRoleServerId> {
    @Query("""
    SELECT r
    FROM UserRoleServer urs
    JOIN Role r ON urs.id.roleId = r.roleId
    WHERE urs.id.userId = :userId
      AND urs.id.serverId = :serverId
""")
    List<Role> findRolesOfUserInServer(
            Long userId,
            Long serverId
    );
}
