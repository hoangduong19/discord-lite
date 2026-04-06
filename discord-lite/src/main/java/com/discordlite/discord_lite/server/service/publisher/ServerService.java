package com.discordlite.discord_lite.server.service.publisher;

import com.discordlite.discord_lite.security.CurrentUserService;
import com.discordlite.discord_lite.server.dto.ServerResponse;
import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.server.repository.ServerRepository;
import com.discordlite.discord_lite.userServer.repository.UserServerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerService {
    private final ServerRepository serverRepository;
    private final ApplicationEventPublisher eventPublisher; // Công cụ để bắn Event
    private final CurrentUserService currentUserService;
    private final UserServerRepository userServerRepository;

    @Transactional
    public Server createServer(String serverName) {
        Long userId = currentUserService.getCurrentUserId();

        // Bước 1: Chỉ tạo và lưu Server
        Server server = new Server();
        server.setServerName(serverName);
        Server savedServer = serverRepository.save(server);

        // Bước 2: Bắn sự kiện đi, các bên liên quan sẽ tự vào "nghe"
        eventPublisher.publishEvent(new ServerCreatedEvent(savedServer, userId));

        return savedServer;
    }

    public List<ServerResponse> getMyServer(Long userId) {
        List<Server> serverList = userServerRepository.findServersByUserId(userId);
        return serverList.stream().map(server -> new ServerResponse(
                server.getServerId(),
                server.getServerName(),
                server.getAvatarUrl()
        )).toList();
    }

    public Server findById(Long serverId) {
        return serverRepository.findById(serverId).orElseThrow(() -> new RuntimeException("Invalid Server"));
    }

    public void save(Server server) {
        serverRepository.save(server);
    }
}
