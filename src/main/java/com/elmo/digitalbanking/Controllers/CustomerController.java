package com.elmo.digitalbanking.Controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elmo.digitalbanking.dto.CustomerDTO;
import com.elmo.digitalbanking.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<CustomerDTO> all() { return service.getAll(); }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (hasAuthority('ROLE_CUSTOMER') and @customerAuthorization.isCustomerAuthorized(#id))")
    public CustomerDTO one(@PathVariable Long id) { return service.getById(id); }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CustomerDTO create(@RequestBody CustomerDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CustomerDTO update(@PathVariable Long id, @RequestBody CustomerDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
