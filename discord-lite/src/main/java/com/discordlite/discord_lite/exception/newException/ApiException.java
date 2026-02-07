package com.discordlite.discord_lite.exception.newException;

import com.discordlite.discord_lite.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;

}
