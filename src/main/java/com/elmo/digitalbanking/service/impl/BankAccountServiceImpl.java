package com.elmo.digitalbanking.service.impl;

import com.elmo.digitalbanking.dto.BankAccountDTO;
import com.elmo.digitalbanking.entities.BankAccount;
import com.elmo.digitalbanking.entities.CurrentAccount;
import com.elmo.digitalbanking.entities.Customer;
import com.elmo.digitalbanking.entities.SavingAccount;
import com.elmo.digitalbanking.repository.BankAccountRepo;
import com.elmo.digitalbanking.repository.CustomerRepo;
import com.elmo.digitalbanking.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepo repo;
    private final CustomerRepo customerRepo;

    private BankAccountDTO toDTO(BankAccount b) {
        BankAccountDTO.BankAccountDTOBuilder builder = BankAccountDTO.builder()
                .id(b.getId())
                .createdAt(b.getCreatedAt())
                .updatedAt(b.getUpdatedAt())
                .balance(b.getBalance())
                .status(b.getStatus())
                .currency(b.getCurrency());

        // Add null check for customer
        if (b.getCustomer() != null) {
            builder.customerId(b.getCustomer().getId());
        }

        BankAccountDTO dto = builder.build();
        if (b instanceof CurrentAccount) dto.setOverDraft(((CurrentAccount)b).getOverDraft());
        if (b instanceof SavingAccount) dto.setInterestRate(((SavingAccount)b).getInterestRate());
        return dto;
    }

    private BankAccount toEntity(BankAccountDTO dto) {
        BankAccount b;
        if (dto.getOverDraft() != null) {
            b = CurrentAccount.builder()
                    .overDraft(dto.getOverDraft())
                    .build();
        } else {
            b = SavingAccount.builder()
                    .interestRate(dto.getInterestRate())
                    .build();
        }
        b.setCreatedAt(dto.getCreatedAt() != null? dto.getCreatedAt(): new Date());
        b.setUpdatedAt(new Date());
        b.setBalance(dto.getBalance());
        b.setStatus(dto.getStatus());
        b.setCurrency(dto.getCurrency());

        // Set customer if customerId is provided
        if (dto.getCustomerId() != null) {
            Customer customer = customerRepo.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + dto.getCustomerId()));
            b.setCustomer(customer);
        }

        return b;
    }

    @Override
    public List<BankAccountDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getById(Long id) {
        return repo.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public BankAccountDTO create(BankAccountDTO dto) {
        BankAccount b = toEntity(dto);
        b = repo.save(b);
        return toDTO(b);
    }

    @Override
    public BankAccountDTO update(Long id, BankAccountDTO dto) {
        BankAccount b = repo.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        b.setBalance(dto.getBalance());
        b.setStatus(dto.getStatus());
        b.setCurrency(dto.getCurrency());
        b.setUpdatedAt(new Date());

        // Update customer if customerId is provided and different from current
        if (dto.getCustomerId() != null && 
            (b.getCustomer() == null || !dto.getCustomerId().equals(b.getCustomer().getId()))) {
            Customer customer = customerRepo.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + dto.getCustomerId()));
            b.setCustomer(customer);
        }

        return toDTO(repo.save(b));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
