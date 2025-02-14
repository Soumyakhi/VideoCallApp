package com.videocallapp.videocallapp.serviceimpl;

import com.videocallapp.videocallapp.entiity.Room;
import com.videocallapp.videocallapp.entiity.Users;
import com.videocallapp.videocallapp.repo.RoomRepo;
import com.videocallapp.videocallapp.repo.UserRepo;
import com.videocallapp.videocallapp.service.CheckIsValid;
import com.videocallapp.videocallapp.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckValidImpl implements CheckIsValid {
    @Autowired
    RoomRepo roomRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public boolean checkValidRoom(HttpServletRequest req, String query) {

        long uid=jwtUtil.extractUserIdFromRequest(req);
        Users users =userRepo.findByUid(uid);
        try{
            Room r1=roomRepo.findByJoineeAndRoomlink(users,query);
            Room r2=roomRepo.findByOwnerAndRoomlink(users,query);
            return r1 != null || r2 != null;
        } catch (Exception e) {
            return false;
        }
    }
}
