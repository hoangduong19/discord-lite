package com.discordlite.discord_lite.channel.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateChannelRequest(
        @NotBlank
        String channelName
) {}

