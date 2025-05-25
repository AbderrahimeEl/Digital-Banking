package com.elmo.digitalbanking.Controllers;

import com.elmo.digitalbanking.dto.CustomerDTO;
import com.elmo.digitalbanking.entities.User;
import com.elmo.digitalbanking.service.CustomerService;
import com.elmo.digitalbanking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;
    private final UserService userService;

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
