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
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
    private Long customerId; // Optional, required only for ROLE_CUSTOMER
}
