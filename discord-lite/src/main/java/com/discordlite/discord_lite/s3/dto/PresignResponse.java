package com.discordlite.discord_lite.s3.dto;

public record PresignResponse(
        String uploadUrl,
        String key
) {
}
