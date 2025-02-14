package com.videocallapp.videocallapp.repo;

import com.videocallapp.videocallapp.entiity.Room;
import com.videocallapp.videocallapp.entiity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepo extends JpaRepository<Room, Long> {
    public Room findByRoomlink(String uuid);

    Room findByOwnerAndRoomlink(Users user,String uuid);
    Room findByJoineeAndRoomlink(Users user,String uuid);
}
