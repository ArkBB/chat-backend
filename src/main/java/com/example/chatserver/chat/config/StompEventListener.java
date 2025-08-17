package com.example.chatserver.chat.config;

import com.example.chatserver.chat.error.UserSessionMaxError;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
@Slf4j
public class StompEventListener {

    private final WebSocketSessionManager webSocketSessionManager;

    public StompEventListener(WebSocketSessionManager webSocketSessionManager) {
        this.webSocketSessionManager = webSocketSessionManager;
    }

    @EventListener
    public void connectHandle(SessionConnectedEvent event) {
        StompHeaderAccessor acc = StompHeaderAccessor.wrap(event.getMessage());
        Authentication auth = (Authentication) acc.getUser(); // 권장
        String sid = acc.getSessionId();

        // 로그/메트릭
        log.info("STOMP CONNECTED: user={}, session={}",
                auth != null ? auth.getName() : "anonymous", sid);
    }

    @EventListener
    public void disconnectHandle(SessionDisconnectEvent event) {

        webSocketSessionManager.unregister(event.getSessionId());

    }
}