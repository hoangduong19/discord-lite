package com.discordlite.discord_lite.auth.dto;

import com.discordlite.discord_lite.auth.enums.VerificationPurpose;

public record SendVerificationEmailRequest (
        String username,
        VerificationPurpose purpose
) {
}
