package com.example.chatserver.chat.config;


import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

//연결 관리를 Stomp가 해주기 때문에
// Event를 Catch해서 log를 남기기 위한 목적
// 연결된 세션수를 실시간으로 확인할 목적
public class StompEventListener {

    private final Set<String> sessions = ConcurrentHashMap.newKeySet();


    @EventListener
    public void connectHandle(SessionConnectedEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sessions.add(accessor.getSessionId());
        System.out.println("connect : " + accessor.getSessionId() + "");
        System.out.println("total sessions : " + sessions.size() + "");

    }

    @EventListener
    public void disconnectHandle(SessionConnectedEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sessions.remove(accessor.getSessionId());
        System.out.println("disconnect : " + accessor.getSessionId() + "");
        System.out.println("total sessions : " + sessions.size() + "");
    }
}
