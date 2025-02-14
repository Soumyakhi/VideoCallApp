package com.videocallapp.videocallapp.controller;
import com.videocallapp.videocallapp.dto.Code;
import com.videocallapp.videocallapp.dto.LoginInfoDTO;
import com.videocallapp.videocallapp.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
public class AuthController {

    @GetMapping("/")
    public String home() {
        return "Hello World";
    }
    @Autowired
    LoginService loginService;
    @PostMapping("/login")
    public ResponseEntity<?> handleGoogleCallback(@RequestBody Code code) {
        try {
            LoginInfoDTO loginInfoDTO=loginService.login(code);
            if(loginInfoDTO!=null) {
                return new ResponseEntity<>(loginInfoDTO, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
