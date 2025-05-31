package com.elmo.digitalbanking.service.impl;

import com.elmo.digitalbanking.dto.TransferDTO;
import com.elmo.digitalbanking.entities.*;
import com.elmo.digitalbanking.exception.AccountNotFoundException;
import com.elmo.digitalbanking.exception.InsufficientFundsException;
import com.elmo.digitalbanking.repository.AccountOperationRepo;
import com.elmo.digitalbanking.repository.BankAccountRepo;
import com.elmo.digitalbanking.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    
    private final BankAccountRepo bankAccountRepo;
    private final AccountOperationRepo operationRepo;
    
    @Override
    @Transactional
    public void transfer(TransferDTO transferDTO) {
        if (transferDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        
        if (transferDTO.getSourceAccountId().equals(transferDTO.getDestinationAccountId())) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same");
        }
        
        // Debit from source account
        debit(transferDTO.getSourceAccountId(), transferDTO.getAmount(), 
              "Transfer to account " + transferDTO.getDestinationAccountId() + ": " + transferDTO.getDescription());
        
        // Credit to destination account
        credit(transferDTO.getDestinationAccountId(), transferDTO.getAmount(), 
               "Transfer from account " + transferDTO.getSourceAccountId() + ": " + transferDTO.getDescription());
    }
    
    @Override
    @Transactional
    public void debit(Long accountId, Double amount, String description) {
        BankAccount account = bankAccountRepo.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("Account is not active");
        }
        
        Double availableBalance = account.getBalance();
        
        // Check for overdraft if it's a CurrentAccount
        if (account instanceof CurrentAccount) {
            CurrentAccount currentAccount = (CurrentAccount) account;
            availableBalance += currentAccount.getOverDraft();
        }
        
        if (availableBalance < amount) {
            throw new InsufficientFundsException("Insufficient funds. Available: " + availableBalance + ", Required: " + amount);
        }
        
        // Update account balance
        account.setBalance(account.getBalance() - amount);
        account.setUpdatedAt(new Date());
        bankAccountRepo.save(account);
        
        // Create operation record
        AccountOperation operation = AccountOperation.builder()
                .date(new Date())
                .amount(amount)
                .type(OperationType.DEBIT)
                .bankAccount(account)
                .build();
        
        operationRepo.save(operation);
    }
    
    @Override
    @Transactional
    public void credit(Long accountId, Double amount, String description) {
        BankAccount account = bankAccountRepo.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("Account is not active");
        }
        
        // Update account balance
        account.setBalance(account.getBalance() + amount);
        account.setUpdatedAt(new Date());
        bankAccountRepo.save(account);
        
        // Create operation record
        AccountOperation operation = AccountOperation.builder()
                .date(new Date())
                .amount(amount)
                .type(OperationType.CREDIT)
                .bankAccount(account)
                .build();
        
        operationRepo.save(operation);
    }
}
