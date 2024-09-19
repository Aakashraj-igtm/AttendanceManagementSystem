package com.attendance.management.service;

import com.attendance.management.dto.LoginRequestDto;
import com.attendance.management.dto.LoginResponseDto;
import com.attendance.management.entity.User;
import com.attendance.management.repo.UserRepository;
import com.attendance.management.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponseDto authenticate(LoginRequestDto jwtRequest) throws Exception {
        User user = userRepository.findByUsername(jwtRequest.getUsername())
                .orElseThrow(() -> new Exception("User not found"));

        // Validate password
        if (!passwordEncoder.matches(jwtRequest.getPassword(), user.getPassword())) {
            throw new Exception("Invalid username or password");
        }

        // Generate JWT token with the user's role
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().getName());
        return new LoginResponseDto(token);
    }
}
