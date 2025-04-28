package com.elmo.digitalbanking.service.impl;

import com.elmo.digitalbanking.dto.CustomerDTO;
import com.elmo.digitalbanking.entities.Customer;
import com.elmo.digitalbanking.repository.CustomerRepo;
import com.elmo.digitalbanking.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepo repo;

    private CustomerDTO toDTO(Customer c) {
        return CustomerDTO.builder()
                .id(c.getId())
                .firstName(c.getFirstName())
                .lastName(c.getLastName())
                .email(c.getEmail())
                .accountIds(c.getAccountSet().stream()
                        .map(a -> a.getId()).collect(Collectors.toList()))
                .build();
    }

    private Customer toEntity(CustomerDTO dto) {
        Customer c = new Customer();
        c.setFirstName(dto.getFirstName());
        c.setLastName(dto.getLastName());
        c.setEmail(dto.getEmail());
        return c;
    }

    @Override
    public List<CustomerDTO> getAll() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getById(Long id) {
        return repo.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public CustomerDTO create(CustomerDTO dto) {
        Customer c = repo.save(toEntity(dto));
        return toDTO(c);
    }

    @Override
    public CustomerDTO update(Long id, CustomerDTO dto) {
        Customer c = repo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        c.setFirstName(dto.getFirstName());
        c.setLastName(dto.getLastName());
        c.setEmail(dto.getEmail());
        return toDTO(repo.save(c));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
