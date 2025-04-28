package com.elmo.digitalbanking.service;

import com.elmo.digitalbanking.dto.CustomerDTO;
import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getAll();
    CustomerDTO getById(Long id);
    CustomerDTO create(CustomerDTO dto);
    CustomerDTO update(Long id, CustomerDTO dto);
    void delete(Long id);
}
