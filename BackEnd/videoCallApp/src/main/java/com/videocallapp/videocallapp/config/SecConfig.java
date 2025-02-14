package com.videocallapp.videocallapp.config;

import com.videocallapp.videocallapp.security.JwtAuthenticationFilter;
import com.videocallapp.videocallapp.security.WsAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecConfig {

    @Autowired
    private JwtAuthenticationFilter filter;

    @Autowired
    private WsAuthenticationFilter wsFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // HTTP Security filter configuration
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                // Define authorization rules for specific HTTP requests first
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/index/**").authenticated() // Secure HTTP paths
                                .requestMatchers("/ws/**").authenticated()   // Secure WebSocket paths
                                .anyRequest().permitAll()
                )
                // Session management (stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Add JWT filter before UsernamePasswordAuthenticationFilter for HTTP requests
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)

                // Add WebSocket-specific filter
                .addFilterBefore(wsFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}



