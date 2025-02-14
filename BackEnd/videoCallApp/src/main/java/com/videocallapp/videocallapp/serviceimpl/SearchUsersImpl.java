package com.videocallapp.videocallapp.serviceimpl;

import com.videocallapp.videocallapp.entiity.Users;
import com.videocallapp.videocallapp.repo.UserRepo;
import com.videocallapp.videocallapp.service.SearchUsers;
import com.videocallapp.videocallapp.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchUsersImpl implements SearchUsers {
    @Autowired
    UserRepo userRepo;
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public List<Users> results(HttpServletRequest request,String query) {
        return userRepo.findUsersByNameLike(jwtUtil.extractUserIdFromRequest(request),"%"+query+"%");
    }
}
