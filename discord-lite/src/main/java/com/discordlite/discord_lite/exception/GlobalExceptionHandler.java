package com.discordlite.discord_lite.exception;

import com.discordlite.discord_lite.exception.newException.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApiException(ApiException ex) {

        HttpStatus status = switch (ex.getErrorCode()) {
            case EMAIL_NOT_VERIFIED, USER_DISABLED -> HttpStatus.FORBIDDEN;
            case INVALID_USERNAME, INVALID_PASSWORD -> HttpStatus.UNAUTHORIZED;
        };

        return ResponseEntity.status(status).body(
                Map.of(
                        "code", ex.getErrorCode().name()
                )
        );
    }
}