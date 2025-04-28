package com.elmo.digitalbanking.repository;

import com.elmo.digitalbanking.entities.CurrentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentAccountRepo extends JpaRepository<CurrentAccount, Long> {
}
