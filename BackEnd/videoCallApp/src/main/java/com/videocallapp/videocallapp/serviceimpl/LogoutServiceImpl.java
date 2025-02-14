package com.videocallapp.videocallapp.serviceimpl;

import com.videocallapp.videocallapp.dto.Code;
import com.videocallapp.videocallapp.dto.LoginInfoDTO;
import com.videocallapp.videocallapp.service.LogoutService;
import com.videocallapp.videocallapp.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
@Service
public class LogoutServiceImpl implements LogoutService {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public void logoutUser(HttpServletRequest request){
        String uid=String.valueOf(jwtUtil.extractUserIdFromRequest(request));
        jwtUtil.removeToken(uid);
    }

}
