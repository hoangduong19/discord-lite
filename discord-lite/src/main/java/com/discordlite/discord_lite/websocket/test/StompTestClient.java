package com.discordlite.discord_lite.websocket.test;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import java.lang.reflect.Type;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class StompTestClient {

    public static void main(String[] args) throws Exception {

        CountDownLatch latch = new CountDownLatch(1);

        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new StringMessageConverter());

        StompSession session = stompClient
                .connectAsync("ws://localhost:8080/ws", new StompSessionHandlerAdapter() {

                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                        System.out.println("✅ STOMP CONNECTED");

                        session.subscribe("/topic/channel/1", new StompFrameHandler() {
                            @Override
                            public Type getPayloadType(StompHeaders headers) {
                                return String.class;
                            }

                            @Override
                            public void handleFrame(StompHeaders headers, Object payload) {
                                System.out.println("📩 RECEIVED: " + payload);
                            }
                        });
                        // 🔥 SEND MESSAGE
                        session.send(
                                "/app/chat.send",
                                """
                                {
                                  "channelId": 1,
                                  "content": "Hello from STOMP test client"
                                }
                                """
                        );

                        latch.countDown();
                    }

                    @Override
                    public void handleTransportError(StompSession session, Throwable exception) {
                        exception.printStackTrace();
                    }
                })
                .get();

        // 🔥 GIỮ JVM SỐNG
        latch.await();
        Thread.sleep(60_000);
    }
}
