package com.discordlite.discord_lite.userServer.repository;

import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.user.entity.User;
import com.discordlite.discord_lite.userServer.compositeKey.UserServerId;
import com.discordlite.discord_lite.userServer.entity.UserServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserServerRepository extends JpaRepository<UserServer, UserServerId> {
    boolean existsByUserAndServer (User user, Server server);
}
