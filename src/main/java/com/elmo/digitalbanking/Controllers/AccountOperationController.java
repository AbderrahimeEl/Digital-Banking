package com.elmo.digitalbanking.Controllers;

import com.elmo.digitalbanking.dto.AccountOperationDTO;
import com.elmo.digitalbanking.service.AccountOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
public class AccountOperationController {
    private final AccountOperationService service;

    @GetMapping
    public List<AccountOperationDTO> all() { return service.getAll(); }

    @GetMapping("/{id}")
    public AccountOperationDTO one(@PathVariable Long id) { return service.getById(id); }

    @PostMapping
    public AccountOperationDTO create(@RequestBody AccountOperationDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    public AccountOperationDTO update(@PathVariable Long id, @RequestBody AccountOperationDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
