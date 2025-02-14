package com.videocallapp.videocallapp.service;

import com.videocallapp.videocallapp.dto.Code;
import com.videocallapp.videocallapp.dto.LoginInfoDTO;

public interface LoginService {
    public LoginInfoDTO login(Code code);
}
