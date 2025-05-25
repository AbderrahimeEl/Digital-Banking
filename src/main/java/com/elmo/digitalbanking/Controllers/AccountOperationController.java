package com.elmo.digitalbanking.Controllers;

import com.elmo.digitalbanking.dto.AccountOperationDTO;
import com.elmo.digitalbanking.security.OperationAuthorization;
import com.elmo.digitalbanking.service.AccountOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
public class AccountOperationController {
    private final AccountOperationService service;
    private final OperationAuthorization operationAuthorization;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<AccountOperationDTO> all() { return service.getAll(); }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (hasAuthority('ROLE_CUSTOMER') and @operationAuthorization.isOperationAuthorized(#id))")
    public AccountOperationDTO one(@PathVariable Long id) { return service.getById(id); }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (hasAuthority('ROLE_CUSTOMER') and @operationAuthorization.canCreateOperation(#dto))")
    public AccountOperationDTO create(@RequestBody AccountOperationDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AccountOperationDTO update(@PathVariable Long id, @RequestBody AccountOperationDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
