package com.discordlite.discord_lite.channelUser.compositeKey;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChannelUserId implements Serializable {
    private Long channelId;
    private Long userId;
}
