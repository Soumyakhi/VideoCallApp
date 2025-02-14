package com.videocallapp.videocallapp.serviceimpl;

import com.videocallapp.videocallapp.entiity.Room;
import com.videocallapp.videocallapp.entiity.Users;
import com.videocallapp.videocallapp.repo.RoomRepo;
import com.videocallapp.videocallapp.repo.UserRepo;
import com.videocallapp.videocallapp.service.ValidateRoom;
import com.videocallapp.videocallapp.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ValidateRoomImpl implements ValidateRoom {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    RoomRepo roomRepo;
    @Autowired
    UserRepo userRepo;
    @Override
    public boolean validateRoom(HttpServletRequest request,String roomid) {
        Users user=userRepo.findByUid(jwtUtil.extractUserIdFromRequest(request));
        Room r1=roomRepo.findByOwnerAndRoomlink(user,roomid);
        Room r2=roomRepo.findByJoineeAndRoomlink(user,roomid);
        return r1!=null || r2!=null;
    }
}
