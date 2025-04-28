package com.elmo.digitalbanking.service;

import com.elmo.digitalbanking.dto.BankAccountDTO;
import java.util.List;

public interface BankAccountService {
    List<BankAccountDTO> getAll();
    BankAccountDTO getById(Long id);
    BankAccountDTO create(BankAccountDTO dto);
    BankAccountDTO update(Long id, BankAccountDTO dto);
    void delete(Long id);
}