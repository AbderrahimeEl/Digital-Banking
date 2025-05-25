package com.elmo.digitalbanking.security;

import com.elmo.digitalbanking.entities.BankAccount;
import com.elmo.digitalbanking.entities.User;
import com.elmo.digitalbanking.repository.BankAccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("accountAuthorization")
@RequiredArgsConstructor
public class AccountAuthorization {
    
    private final BankAccountRepo bankAccountRepo;
    
    public boolean isAccountOwner(Long accountId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        User user = (User) authentication.getPrincipal();
        if (user.getCustomerId() == null) {
            return false;
        }
        
        Optional<BankAccount> accountOpt = bankAccountRepo.findById(accountId);
        if (accountOpt.isEmpty()) {
            return false;
        }
        
        BankAccount account = accountOpt.get();
        return account.getCustomer() != null && 
               account.getCustomer().getId() != null && 
               account.getCustomer().getId().equals(user.getCustomerId());
    }
}
