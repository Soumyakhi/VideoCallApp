package com.videocallapp.videocallapp.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String userId = extractUserIdFromToken(authHeader);
        if (userId != null) {
            attributes.put("userId", Long.parseLong(userId));
            return true;
        } else {
            return false;
        }
    }
    private String extractUserIdFromToken(String token) {
        JwtUtil jwtUtil=new JwtUtil();
        return jwtUtil.extractUserId(token.substring(7));
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
