package com.discordlite.discord_lite.role.repository;

import com.discordlite.discord_lite.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByServerIdAndRoleName (Long serverId, String roleName);
}
