package com.elmo.digitalbanking.security;

import com.elmo.digitalbanking.entities.User;
import com.elmo.digitalbanking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("customerAuthorization")
@RequiredArgsConstructor
public class CustomerAuthorization {
    
    private final UserService userService;
    
    public boolean isCustomerAuthorized(Long customerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        User user = (User) authentication.getPrincipal();
        return user.getCustomerId() != null && user.getCustomerId().equals(customerId);
    }
}
