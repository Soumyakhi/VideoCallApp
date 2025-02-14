package com.videocallapp.videocallapp.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;

public interface ValidateRoom {
    public boolean validateRoom(HttpServletRequest request,String roomid);
}
