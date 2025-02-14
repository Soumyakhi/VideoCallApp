package com.videocallapp.videocallapp.service;

import com.videocallapp.videocallapp.dto.LoginInfoDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.net.http.HttpRequest;

public interface LogoutService {
    public void logoutUser(HttpServletRequest request);
}
