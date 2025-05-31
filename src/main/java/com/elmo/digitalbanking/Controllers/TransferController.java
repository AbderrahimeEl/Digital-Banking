package com.elmo.digitalbanking.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elmo.digitalbanking.dto.TransferDTO;
import com.elmo.digitalbanking.service.TransferService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferDTO transferDTO) {
        transferService.transfer(transferDTO);
        return ResponseEntity.ok("Transfer completed successfully");
    }

    @PostMapping("/debit/{accountId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> debit(@PathVariable Long accountId,
                                       @RequestParam Double amount,
                                       @RequestParam(required = false, defaultValue = "Manual debit") String description) {
        transferService.debit(accountId, amount, description);
        return ResponseEntity.ok("Debit operation completed successfully");
    }

    @PostMapping("/credit/{accountId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> credit(@PathVariable Long accountId,
                                        @RequestParam Double amount,
                                        @RequestParam(required = false, defaultValue = "Manual credit") String description) {
        transferService.credit(accountId, amount, description);
        return ResponseEntity.ok("Credit operation completed successfully");
    }
}
