package com.discordlite.discord_lite.userServer.compositeKey;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserServerId implements Serializable {
    private Long userId;
    private Long serverId;

}
