package com.videocallapp.videocallapp.service;

import jakarta.servlet.http.HttpServletRequest;

public interface CheckIsValid {
    public boolean checkValidRoom(HttpServletRequest req, String query);
}
