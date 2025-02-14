package com.videocallapp.videocallapp.controller;

import com.videocallapp.videocallapp.entiity.Users;
import com.videocallapp.videocallapp.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RequestMapping("/index")
@RestController
public class FilteredController {
    @Autowired
    private LogoutService logout;
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        logout.logoutUser(request);
        return "logged out";
    }
    @GetMapping("/dummyreq")
    public String home() {
        return "Hello World";
    }
    @Autowired
    private SearchUsers searchUsers;
    @GetMapping("/search/{query}")
    public List<Users> search(HttpServletRequest request,@PathVariable String query){
        return searchUsers.results(request, query);
    }
    @Autowired
    private CreateRoom createRoom;
    @PostMapping("/CreateRoom/{joineeId}")
    public ResponseEntity<Object> createRoom(HttpServletRequest request, @PathVariable String joineeId){
        try{
            String res=createRoom.createRoom(request,Long.parseLong(joineeId));
            if(res.equals("400")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("can not create room");
            }
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("can not create room");
    }
    @Autowired
    private CheckIsValid checkIsValid;
    @GetMapping("/isValid/{query}")
    public ResponseEntity<Object> checkValid(HttpServletRequest request,@PathVariable String query){
        if(checkIsValid.checkValidRoom(request, query)){
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }
    @Autowired
    private ValidateRoom validateRoom;
    @GetMapping("/isValidRoom/{roomid}")
    public ResponseEntity<Object> isValidRoom(HttpServletRequest request,@PathVariable String roomid){
        if(checkIsValid.checkValidRoom(request, roomid)){
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

}
