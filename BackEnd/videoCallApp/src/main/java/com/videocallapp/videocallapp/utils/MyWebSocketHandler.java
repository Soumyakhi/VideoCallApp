package com.videocallapp.videocallapp.utils;
import com.videocallapp.videocallapp.repo.RoomRepo;
import com.videocallapp.videocallapp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, Set<WebSocketSession>> groupSessions = new ConcurrentHashMap<>();
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String uuid = (session.getUri().getPath().split("/")[2]);
        Long userId = Long.parseLong(jwtUtil.extractUserId(session.getUri().getPath().split("/")[3]));
        if (isValidRoomMember(userId, uuid)) {
            groupSessions.computeIfAbsent(uuid, k -> new HashSet<>()).add(session);
            System.out.println("User " + userId + " connected to room " + uuid);
        } else {
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }
    }
    @Autowired
    RoomRepo roomRepo;
    @Autowired
    UserRepo userRepo;
    public boolean isValidRoomMember(Long userId, String uuid) {
        System.out.println(uuid);
        return (roomRepo.findByOwnerAndRoomlink(userRepo.findByUid(userId),uuid)!=null ||
                roomRepo.findByJoineeAndRoomlink(userRepo.findByUid(userId),uuid)!=null);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String uuid = session.getUri().getPath().split("/")[2];
        Set<WebSocketSession> groupSessionSet = groupSessions.getOrDefault(uuid, new HashSet<>());
        for (WebSocketSession s : groupSessionSet) {
            if (s.isOpen() && !s.equals(session)) {
                s.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String uuid = (session.getUri().getPath().split("/")[2]);
        Long userId = Long.parseLong(jwtUtil.extractUserId(session.getUri().getPath().split("/")[3]));
        Set<WebSocketSession> groupSessionSet = groupSessions.getOrDefault(uuid, new HashSet<>());
        groupSessionSet.remove(session);
        if (groupSessionSet.isEmpty()) groupSessions.remove(uuid);
        System.out.println("User"+userId+" disconnected from group " + uuid);
    }
}
