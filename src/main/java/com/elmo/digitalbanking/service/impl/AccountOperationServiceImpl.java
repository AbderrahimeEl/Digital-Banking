package com.elmo.digitalbanking.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elmo.digitalbanking.dto.AccountOperationDTO;
import com.elmo.digitalbanking.entities.AccountOperation;
import com.elmo.digitalbanking.entities.BankAccount;
import com.elmo.digitalbanking.entities.OperationType;
import com.elmo.digitalbanking.exception.AccountNotFoundException;
import com.elmo.digitalbanking.repository.AccountOperationRepo;
import com.elmo.digitalbanking.repository.BankAccountRepo;
import com.elmo.digitalbanking.service.AccountOperationService;
import com.elmo.digitalbanking.service.TransferService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountOperationServiceImpl implements AccountOperationService {
    private final AccountOperationRepo repo;
    private final BankAccountRepo accountRepo;
    private final TransferService transferService;

    private AccountOperationDTO toDTO(AccountOperation op) {
        return AccountOperationDTO.builder()
                .id(op.getId())
                .date(op.getDate())
                .amount(op.getAmount())
                .type(op.getType())
                .bankAccountId(op.getBankAccount().getId())
                .build();
    }

    private AccountOperation toEntity(AccountOperationDTO dto) {
        AccountOperation op = new AccountOperation();
        op.setDate(dto.getDate());
        op.setAmount(dto.getAmount());
        op.setType(dto.getType());
        BankAccount b = accountRepo.findById(dto.getBankAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        op.setBankAccount(b);
        return op;
    }

    @Override
    public List<AccountOperationDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public AccountOperationDTO getById(Long id) {
        return repo.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Operation not found"));
    }

    @Override
    @Transactional
    public AccountOperationDTO create(AccountOperationDTO dto) {
        // Use transfer service to ensure balance updates
        if (dto.getType() == OperationType.DEBIT) {
            transferService.debit(dto.getBankAccountId(), dto.getAmount(), "Manual debit operation");
        } else if (dto.getType() == OperationType.CREDIT) {
            transferService.credit(dto.getBankAccountId(), dto.getAmount(), "Manual credit operation");
        }

        // Return the latest operation for this account
        BankAccount account = accountRepo.findById(dto.getBankAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        // Find the most recent operation for this account
        return account.getOperations().stream()
                .filter(op -> op.getType() == dto.getType() && Double.compare(op.getAmount(), dto.getAmount()) == 0)
                .max((op1, op2) -> op1.getDate().compareTo(op2.getDate()))
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Operation not found after creation"));
    }

    @Override
    public AccountOperationDTO update(Long id, AccountOperationDTO dto) {
        AccountOperation op = repo.findById(id).orElseThrow(() -> new RuntimeException("Op not found"));
        op.setAmount(dto.getAmount());
        op.setDate(dto.getDate());
        op.setType(dto.getType());
        return toDTO(repo.save(op));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
