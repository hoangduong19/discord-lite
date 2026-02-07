package com.discordlite.discord_lite.security.stomp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
@Getter
public class StompPrincipal implements Principal {
    private final Long userId;

    @Override
    public String getName() {
        // Spring STOMP chỉ dung` String
        return userId.toString();
    }
}
