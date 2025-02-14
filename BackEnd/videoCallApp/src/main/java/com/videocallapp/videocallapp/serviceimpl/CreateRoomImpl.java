package com.videocallapp.videocallapp.serviceimpl;

import com.videocallapp.videocallapp.entiity.Room;
import com.videocallapp.videocallapp.entiity.Users;
import com.videocallapp.videocallapp.repo.RoomRepo;
import com.videocallapp.videocallapp.repo.UserRepo;
import com.videocallapp.videocallapp.service.CreateRoom;
import com.videocallapp.videocallapp.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateRoomImpl implements CreateRoom {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepo userRepo;
    @Autowired
    RoomRepo roomRepo;
    @Override
    public String createRoom(HttpServletRequest req, long joineeId) {
        long ownerId = jwtUtil.extractUserIdFromRequest(req);
        Users owner = userRepo.findByUid(ownerId);
        Users joinee = userRepo.findByUid(joineeId);
        if(owner!=null && joinee!=null && ownerId!=joineeId) {
            Room room = new Room();
            room.setOwner(owner);
            room.setJoinee(joinee);
            String uuid = UUID.randomUUID().toString();
            room.setRoomlink(uuid);
            roomRepo.save(room);
            return uuid;
        }
        return "400";
    }
}
