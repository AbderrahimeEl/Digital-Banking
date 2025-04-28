package com.elmo.digitalbanking.controller;

import com.elmo.digitalbanking.dto.CustomerDTO;
import com.elmo.digitalbanking.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @GetMapping
    public List<CustomerDTO> all() { return service.getAll(); }

    @GetMapping("/{id}")
    public CustomerDTO one(@PathVariable Long id) { return service.getById(id); }

    @PostMapping
    public CustomerDTO create(@RequestBody CustomerDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    public CustomerDTO update(@PathVariable Long id, @RequestBody CustomerDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
