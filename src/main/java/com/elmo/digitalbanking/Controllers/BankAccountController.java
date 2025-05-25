package com.elmo.digitalbanking.Controllers;

import com.elmo.digitalbanking.dto.BankAccountDTO;
import com.elmo.digitalbanking.security.AccountAuthorization;
import com.elmo.digitalbanking.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BankAccountController {
    private final BankAccountService service;
    private final AccountAuthorization accountAuthorization;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<BankAccountDTO> all() { return service.getAll(); }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (hasAuthority('ROLE_CUSTOMER') and @accountAuthorization.isAccountOwner(#id))")
    public BankAccountDTO one(@PathVariable Long id) { return service.getById(id); }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public BankAccountDTO create(@RequestBody BankAccountDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public BankAccountDTO update(@PathVariable Long id, @RequestBody BankAccountDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(@PathVariable Long id) { service.delete(id); }
}