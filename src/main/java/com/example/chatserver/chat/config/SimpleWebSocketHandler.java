//package com.example.chatserver.chat.config;
//
//import lombok.extern.slf4j.Slf4j;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//@Slf4j
//@Configuration
//public class SimpleWebSocketHandler extends TextWebSocketHandler {
//
//    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        sessions.add(session);
//        log.info("새로운 웹소켓 연결이 설정되었습니다. 세션 ID: {}", session.getId());
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//
//        log.info("메시지 수신: {} (세션 ID: {})", payload, session.getId());
//
//        for(WebSocketSession s : sessions){
//            if(s.isOpen()){
//                s.sendMessage(new TextMessage(payload));
//            }
//        }
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessions.remove(session);
//        log.info("웹소켓 연결이 종료되었습니다. 세션 ID: {}, 상태: {}", session.getId(), status);
//    }
//}