package com.videocallapp.videocallapp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CreateRoom {
    public String createRoom(HttpServletRequest req, long joineeId);
}
