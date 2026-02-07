package com.discordlite.discord_lite.security.stomp;

import com.discordlite.discord_lite.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtStompInterceptor implements ChannelInterceptor {
    private final JwtService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class); //fix null user
        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");//do Authorization: Bearer ...

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Missing Authorization header");
            }

            String token = authHeader.substring(7);

            if (jwtService.isExpired(token)) {
                throw new RuntimeException("JWT expired");
            }

            Long userId = jwtService.extractUserId(token);

            accessor.setUser(new StompPrincipal(userId));
            System.out.println("📨 SEND from user = " + accessor.getUser());
        }

        return message;
    }
}
