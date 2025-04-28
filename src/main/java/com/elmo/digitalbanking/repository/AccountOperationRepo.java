package com.elmo.digitalbanking.repository;

import com.elmo.digitalbanking.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepo extends JpaRepository<AccountOperation, Long> {
}
