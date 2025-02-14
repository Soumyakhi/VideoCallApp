package com.videocallapp.videocallapp.service;

import com.videocallapp.videocallapp.entiity.Users;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SearchUsers {
    public List<Users> results(HttpServletRequest request,String query);
}
