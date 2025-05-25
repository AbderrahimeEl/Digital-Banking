package com.elmo.digitalbanking.dto;

import com.elmo.digitalbanking.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String refreshToken;
    private Long userId;
    private Role role;
    private Long customerId; // Will be null for admin users
}
