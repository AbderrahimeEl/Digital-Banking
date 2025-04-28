package com.elmo.digitalbanking.repository;

import com.elmo.digitalbanking.entities.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingAccountRepo extends JpaRepository<SavingAccount, Long> {
}
