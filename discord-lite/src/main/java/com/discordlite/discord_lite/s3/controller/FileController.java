package com.discordlite.discord_lite.s3.controller;

import com.discordlite.discord_lite.s3.dto.PresignResponse;
import com.discordlite.discord_lite.s3.service.S3Service;
import com.discordlite.discord_lite.security.CurrentUserService;
import com.discordlite.discord_lite.user.entity.User;
import com.discordlite.discord_lite.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final S3Service s3Service;
    private final CurrentUserService currentUserService;
    private final UserService userService;

    @PostMapping("/presign-user-avatar")
    public ResponseEntity<PresignResponse> presignAvatar() {

        Long userId = currentUserService.getCurrentUserId();
        String key = "users/" + userId + "/" + UUID.randomUUID() + ".jpg";

        String presignedUrl = s3Service.generatePresignedUploadUrl(key);

        return ResponseEntity.ok(new PresignResponse(presignedUrl, key));
    }

    @PostMapping("/confirm-user-avatar")
    public ResponseEntity<Void> confirmAvatar(@RequestParam String key) {

        Long userId = currentUserService.getCurrentUserId();

        if (!key.startsWith("users/" + userId + "/")) {
            throw new RuntimeException("Invalid key");
        }

        User user = userService.findById(userId);
        user.setAvatar(key);   // SAVE KEY, not URL
        userService.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-user-avatar")
    public ResponseEntity<String> getAvatarUrl() {

        Long userId = currentUserService.getCurrentUserId();
        User user = userService.findById(userId);

        String presignedUrl = s3Service.generatePresignedUrl(user.getAvatar());

        return ResponseEntity.ok(presignedUrl);
    }
}
