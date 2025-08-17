package com.example.chatserver.chat.config;

import com.example.chatserver.common.auth.JwtTokenProvider;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class StompHandler implements ChannelInterceptor {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final WebSocketSessionManager webSocketSessionManager;
    private final JwtTokenProvider jwtTokenProvider;

    public StompHandler(WebSocketSessionManager webSocketSessionManager, JwtTokenProvider jwtTokenProvider) {
        this.webSocketSessionManager = webSocketSessionManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //connect 하기 전, describe 하기 전, send 하기 전, disconnect 하기 전 무조건 타는 메소드
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 1. 헤더에서 JWT 추출
            String token = resolveToken(accessor.getFirstNativeHeader("Authorization"));

            // 2. 토큰 유효성 검사
            if (token == null && jwtTokenProvider.validateToken(token)) {
                throw new AuthenticationServiceException("웹소켓 연결에 유효하지 않은 토큰입니다.");
            }

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            accessor.setUser(authentication);

            String user = authentication.getName();
            String sessionId = accessor.getSessionId(); // simpSessionId 사용 가능

            boolean ok = webSocketSessionManager.tryRegister(sessionId, user);

            if (!ok)
                throw new AuthenticationServiceException("Max sessions reached");

            if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                webSocketSessionManager.unregister(accessor.getSessionId());
            }

        }
        // 만약 send인 경우에도 매 프레임마다 auth 검증 하고 싶으면 추가, 실무에선 어떻게 하는지 모르겠음.

        return message;
    }

    private String resolveToken (String bearerToken){
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}




