package com.videocallapp.videocallapp.security;

import com.videocallapp.videocallapp.entiity.Users;
import com.videocallapp.videocallapp.repo.UserRepo;
import com.videocallapp.videocallapp.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
public class WsAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepo userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractTokenFromUrl(request);
        String userId = null;
        if (token != null) {
            try {
                userId = jwtUtil.extractUserId(token);
                if (userId != null) {
                    Users user = userRepo.findByUid(Long.parseLong(userId));
                    if (user != null && jwtUtil.validateToken(token, user.getUid())) {
                        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(user, null, authorities);
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {

            }
        }
        filterChain.doFilter(request, response);
    }
    private String extractTokenFromUrl(HttpServletRequest request) {
        String token = null;
        String[] pathSegments = request.getRequestURI().split("/");
            token = pathSegments[pathSegments.length - 1];
        return token;
    }
}
