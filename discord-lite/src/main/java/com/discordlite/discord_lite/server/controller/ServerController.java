package com.discordlite.discord_lite.server.controller;

import com.discordlite.discord_lite.server.dto.CreateServerRequest;
import com.discordlite.discord_lite.server.dto.CreateServerResponse;
import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.server.service.ServerService;
import com.discordlite.discord_lite.userServer.dto.JoinServerRequest;
import com.discordlite.discord_lite.userServer.dto.JoinServerResponse;
import com.discordlite.discord_lite.userServer.service.UserServerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/join")
    public ResponseEntity<JoinServerResponse> joinServer (
            @Valid @RequestBody JoinServerRequest request) {
        JoinServerResponse response = userServerService.joinServer(request.username(), request.inviteCode());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
