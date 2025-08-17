package com.example.chatserver.chat.config;

import com.example.chatserver.chat.error.UserSessionMaxError;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WebSocketSessionManager {

    private static final int MAX_SESSIONS_PER_USER = 3;

    // user -> sessions
    private final ConcurrentMap<String, Set<String>> userSessions = new ConcurrentHashMap<>();
    // sessionId -> user (빠른 제거용)
    private final ConcurrentMap<String, String> sessionOwner = new ConcurrentHashMap<>();

    /** CONNECT 시점 예약(등록). 성공 시 true */
    public boolean tryRegister(String sessionId, String user) {
        final Set<String> added = userSessions.compute(user, (k, set) -> {
            if (set == null) set = ConcurrentHashMap.newKeySet();
            if (set.contains(sessionId)) return set; // 재진입 방지
            if (set.size() >= MAX_SESSIONS_PER_USER) return set; // 넘치면 그대로 반환
            set.add(sessionId);
            return set;
        });
        boolean success = added.contains(sessionId);
        if (success) {
            sessionOwner.put(sessionId, user);
            log.info("세션 등록: user={}, total={}", user, added.size());
        } else {
            log.warn("세션 한도 초과: user={}, current={}", user, added.size());
        }
        return success;
    }

    /** DISCONNECT 시점 해제 */
    public void unregister(String sessionId) {
        String user = sessionOwner.remove(sessionId);
        if (user == null) return;

        userSessions.computeIfPresent(user, (k, set) -> {
            set.remove(sessionId);
            log.info("세션 제거: user={}, remaining={}", user, set.size());
            return set.isEmpty() ? null : set;
        });
    }

    public int count(String user) {
        return userSessions.getOrDefault(user, Set.of()).size();
    }
}



