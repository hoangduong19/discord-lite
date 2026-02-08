package com.discordlite.discord_lite.server.controller;

import com.discordlite.discord_lite.security.CurrentUserService;
import com.discordlite.discord_lite.server.dto.CreateServerRequest;
import com.discordlite.discord_lite.server.dto.CreateServerResponse;
import com.discordlite.discord_lite.server.dto.ServerResponse;
import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.server.service.ServerService;
import com.discordlite.discord_lite.userServer.dto.JoinServerRequest;
import com.discordlite.discord_lite.userServer.dto.JoinServerResponse;
import com.discordlite.discord_lite.userServer.service.UserServerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/servers")
public class ServerController {
    private final ServerService serverService;
    private final UserServerService userServerService;

    @PostMapping("/create")
    public ResponseEntity<CreateServerResponse> createServer(
            @Valid @RequestBody CreateServerRequest request) {

        Server server = serverService.createServer(request.serverName());


        CreateServerResponse response = new CreateServerResponse(
                server.getServerId(),
                server.getServerName(),
                server.getInviteCode().getInviteCodeLink()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-servers")
    public List<ServerResponse> getMyServers(CurrentUserService currentUserService) {
        Long userId = currentUserService.getCurrentUserId();
        return serverService.getMyServer(userId);
    }

    @PostMapping("/join")
    public ResponseEntity<JoinServerResponse> joinServer (
            @Valid @RequestBody JoinServerRequest request) {
        JoinServerResponse response = userServerService.joinServer(request.inviteCode());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
