package com.elmo.digitalbanking.service.impl;

import com.elmo.digitalbanking.dto.AccountOperationDTO;
import com.elmo.digitalbanking.entities.AccountOperation;
import com.elmo.digitalbanking.entities.BankAccount;
import com.elmo.digitalbanking.repository.AccountOperationRepo;
import com.elmo.digitalbanking.repository.BankAccountRepo;
import com.elmo.digitalbanking.service.AccountOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountOperationServiceImpl implements AccountOperationService {
    private final AccountOperationRepo repo;
    private final BankAccountRepo accountRepo;

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
    public AccountOperationDTO create(AccountOperationDTO dto) {
        AccountOperation op = repo.save(toEntity(dto));
        return toDTO(op);
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
