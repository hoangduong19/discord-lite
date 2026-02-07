package com.discordlite.discord_lite.userRoleServer.entity;

import com.discordlite.discord_lite.userRoleServer.compositeKey.UserRoleServerId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="user_role_server")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRoleServer {
    @EmbeddedId
    private UserRoleServerId id;

}
