package com.discordlite.discord_lite.s3.controller;

import com.discordlite.discord_lite.s3.dto.PresignServerRequest;
import com.discordlite.discord_lite.s3.dto.PresignUserRequest;
import com.discordlite.discord_lite.s3.dto.PresignResponse;
import com.discordlite.discord_lite.s3.service.S3Service;
import com.discordlite.discord_lite.security.CurrentUserService;
import com.discordlite.discord_lite.server.dto.ConfirmServerRequest;
import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.server.service.ServerService;
import com.discordlite.discord_lite.user.entity.User;
import com.discordlite.discord_lite.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final S3Service s3Service;
    private final CurrentUserService currentUserService;
    private final UserService userService;
    private final ServerService serverService;

    //User
    @PostMapping("/presign-user-avatar")
    public ResponseEntity<PresignResponse> presignAvatarUser() {

        Long userId = currentUserService.getCurrentUserId();
        String key = "users/" + userId + "/" + UUID.randomUUID() + ".jpg";

        String presignedUrl = s3Service.generatePresignedUploadUrl(key);

        return ResponseEntity.ok(new PresignResponse(presignedUrl, key));
    }

    @PostMapping("/confirm-user-avatar")
    public ResponseEntity<Void> confirmAvatarUser(@RequestBody PresignUserRequest presignUserRequest) {

        Long userId = currentUserService.getCurrentUserId();
        String key = presignUserRequest.key();

        if (!key.startsWith("users/" + userId + "/")) {
            throw new RuntimeException("Invalid key");
        }

        User user = userService.findById(userId);
        user.setAvatar(key);   // SAVE KEY, not URL
        userService.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-user-avatar")
    public ResponseEntity<String> getAvatarUrlUser() {

        Long userId = currentUserService.getCurrentUserId();
        User user = userService.findById(userId);

        String presignedUrl = s3Service.generatePresignedUrl(user.getAvatar());

        return ResponseEntity.ok(presignedUrl);
    }


    //Server
    @PostMapping("/presign-server-avatar")
    public ResponseEntity<PresignResponse> presignAvatarServer(@RequestBody PresignServerRequest presignServerRequest) {
        Long serverId = presignServerRequest.serverId();
        String key = "servers/" + serverId + "/" + UUID.randomUUID() + ".jpg";

        String presignedUrl = s3Service.generatePresignedUploadUrl(key);

        return ResponseEntity.ok(new PresignResponse(presignedUrl, key));
    }

    @PostMapping("/confirm-server-avatar")
    public ResponseEntity<Void> confirmAvatarServer(@RequestBody ConfirmServerRequest confirmServerRequest) {

        Long serverId = confirmServerRequest.serverId();
        String key = confirmServerRequest.key();

        if (!key.startsWith("servers/" + serverId + "/")) {
            throw new RuntimeException("Invalid key");
        }

        Server server = serverService.findById(serverId);
        server.setAvatarUrl(key);
        serverService.save(server);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-server-avatar")
    public ResponseEntity<String> getAvatarUrlServer(@RequestParam Long serverId) {

        Server server = serverService.findById(serverId);

        String presignedUrl = s3Service.generatePresignedUrl(server.getAvatarUrl());

        return ResponseEntity.ok(presignedUrl);
    }
}
