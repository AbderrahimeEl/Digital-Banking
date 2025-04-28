package com.elmo.digitalbanking.repository;

import com.elmo.digitalbanking.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {
}
