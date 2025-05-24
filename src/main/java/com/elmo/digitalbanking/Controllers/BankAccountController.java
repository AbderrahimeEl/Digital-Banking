package com.elmo.digitalbanking.Controllers;

import com.elmo.digitalbanking.dto.BankAccountDTO;
import com.elmo.digitalbanking.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BankAccountController {
    private final BankAccountService service;

    @GetMapping
    public List<BankAccountDTO> all() { return service.getAll(); }

    @GetMapping("/{id}")
    public BankAccountDTO one(@PathVariable Long id) { return service.getById(id); }

    @PostMapping
    public BankAccountDTO create(@RequestBody BankAccountDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    public BankAccountDTO update(@PathVariable Long id, @RequestBody BankAccountDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}