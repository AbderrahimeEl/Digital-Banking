package com.elmo.digitalbanking.service;

import com.elmo.digitalbanking.dto.TransferDTO;

public interface TransferService {
    void transfer(TransferDTO transferDTO);
    void debit(Long accountId, Double amount, String description);
    void credit(Long accountId, Double amount, String description);
}
