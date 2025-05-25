package com.elmo.digitalbanking.service;

import com.elmo.digitalbanking.dto.AuthRequest;
import com.elmo.digitalbanking.dto.AuthResponse;
import com.elmo.digitalbanking.dto.RegisterRequest;
import com.elmo.digitalbanking.entities.Role;
import com.elmo.digitalbanking.entities.User;
import com.elmo.digitalbanking.repository.CustomerRepo;
import com.elmo.digitalbanking.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserService userService;
    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    
    public AuthResponse register(RegisterRequest request) {
        // Validate if username already exists
        if (userService.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // If role is CUSTOMER, validate that customer exists
        if (request.getRole() == Role.ROLE_CUSTOMER && request.getCustomerId() != null) {
            if (!customerRepo.existsById(request.getCustomerId())) {
                throw new RuntimeException("Customer not found with ID: " + request.getCustomerId());
            }
            
            // Check if this customer already has a user account
            if (userService.findByCustomerId(request.getCustomerId()).isPresent()) {
                throw new RuntimeException("This customer already has a user account");
            }
        }
        
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .customerId(request.getRole() == Role.ROLE_CUSTOMER ? request.getCustomerId() : null)
                .build();
                
        user = userService.saveUser(user);
        
        String jwtToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        
        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .role(user.getRole())
                .customerId(user.getCustomerId())
                .build();
    }
    
    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        
        String jwtToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        
        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .role(user.getRole())
                .customerId(user.getCustomerId())
                .build();
    }
    
    public AuthResponse refreshToken(String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken);
        
        if (username != null) {
            User user = userService.loadUserByUsername(username);
            
            if (jwtUtil.isTokenValid(refreshToken, user)) {
                String newToken = jwtUtil.generateToken(user);
                String newRefreshToken = jwtUtil.generateRefreshToken(user);
                
                return AuthResponse.builder()
                        .token(newToken)
                        .refreshToken(newRefreshToken)
                        .userId(user.getId())
                        .role(user.getRole())
                        .customerId(user.getCustomerId())
                        .build();
            }
        }
        
        throw new RuntimeException("Invalid refresh token");
    }
}
