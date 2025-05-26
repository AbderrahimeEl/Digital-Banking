package com.elmo.digitalbanking.security;

import com.elmo.digitalbanking.dto.AccountOperationDTO;
import com.elmo.digitalbanking.entities.AccountOperation;
import com.elmo.digitalbanking.entities.User;
import com.elmo.digitalbanking.repository.AccountOperationRepo;
import com.elmo.digitalbanking.repository.BankAccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("operationAuthorization")
@RequiredArgsConstructor
public class OperationAuthorization {
    
    private final AccountOperationRepo operationRepo;
    private final AccountAuthorization accountAuthorization;
    
    public boolean isOperationAuthorized(Long operationId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return false;
//        }
//
//        User user = (User) authentication.getPrincipal();
//        if (user.getCustomerId() == null) {
//            return false;
//        }
        
        Optional<AccountOperation> operationOpt = operationRepo.findById(operationId);
        if (operationOpt.isEmpty()) {
            return false;
        }
        
        AccountOperation operation = operationOpt.get();
        if (operation.getBankAccount() == null) {
            return false;
        }
        
        return accountAuthorization.isAccountOwner(operation.getBankAccount().getId());
    }
    
    public boolean canCreateOperation(AccountOperationDTO operationDTO) {
        if (operationDTO == null || operationDTO.getBankAccountId() == null) {
            return false;
        }
        
        return accountAuthorization.isAccountOwner(operationDTO.getBankAccountId());
    }
}
