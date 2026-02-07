package com.discordlite.discord_lite.userRoleServer.compositeKey;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserRoleServerId implements Serializable {
    private Long userId;
    private Long roleId;
    private Long serverId;

}
