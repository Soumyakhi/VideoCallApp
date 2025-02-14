package com.videocallapp.videocallapp.serviceimpl;

import com.videocallapp.videocallapp.dto.Code;
import com.videocallapp.videocallapp.dto.LoginInfoDTO;
import com.videocallapp.videocallapp.entiity.Users;
import com.videocallapp.videocallapp.repo.UserRepo;
import com.videocallapp.videocallapp.service.LoginService;
import com.videocallapp.videocallapp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public LoginInfoDTO login(Code code) {
        try {
            String tokenEndpoint = "https://oauth2.googleapis.com/token";
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code.getCodeStr());
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", "http://localhost:5173/login");
            params.add("grant_type", "authorization_code");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            String idToken = (String) tokenResponse.getBody().get("id_token");
            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);
            if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> userInfo = userInfoResponse.getBody();
                String email = (String) userInfo.get("email");
                String userName=email.substring(0,email.indexOf("@"));
                String sub = (String) userInfo.get("sub");
                Users user = userRepo.findByEmail(email);
                if(user == null) {
                    user = new Users();
                    user.setEmail(email);
                    user.setUname(userName);
                    user.setSub(sub);
                    userRepo.save(user);
                }
                user = userRepo.findByEmail(email);
                String jwtToken = jwtUtil.generateToken(user.getUid().toString());
                LoginInfoDTO loginInfoDTO=new LoginInfoDTO();
                loginInfoDTO.setUsername(userName);
                loginInfoDTO.setSub(sub);
                loginInfoDTO.setEmail(email);
                loginInfoDTO.setJwtToken(jwtToken);
                return loginInfoDTO;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
