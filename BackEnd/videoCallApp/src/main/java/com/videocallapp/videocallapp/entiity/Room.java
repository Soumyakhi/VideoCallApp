package com.videocallapp.videocallapp.entiity;

import jakarta.persistence.*;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomid;
    @ManyToOne
    @JoinColumn(name = "ownerid", referencedColumnName = "uid", nullable = false)
    private Users owner;
    @ManyToOne
    @JoinColumn(name = "joineeid", referencedColumnName = "uid", nullable = false)
    private Users joinee;
    private String roomlink;

    public Long getRoomid() {
        return roomid;
    }

    public void setRoomid(Long roomid) {
        this.roomid = roomid;
    }

    public Users getOwner() {
        return owner;
    }

    public void setOwner(Users owner) {
        this.owner = owner;
    }

    public Users getJoinee() {
        return joinee;
    }

    public void setJoinee(Users joinee) {
        this.joinee = joinee;
    }

    public String getRoomlink() {
        return roomlink;
    }

    public void setRoomlink(String roomlink) {
        this.roomlink = roomlink;
    }
}
