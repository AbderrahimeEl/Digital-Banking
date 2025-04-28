package com.elmo.digitalbanking.service;

import com.elmo.digitalbanking.dto.AccountOperationDTO;
import java.util.List;

public interface AccountOperationService {
    List<AccountOperationDTO> getAll();
    AccountOperationDTO getById(Long id);
    AccountOperationDTO create(AccountOperationDTO dto);
    AccountOperationDTO update(Long id, AccountOperationDTO dto);
    void delete(Long id);
}