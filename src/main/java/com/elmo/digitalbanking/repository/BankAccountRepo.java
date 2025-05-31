package com.elmo.digitalbanking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.elmo.digitalbanking.entities.AccountStatus;
import com.elmo.digitalbanking.entities.BankAccount;

public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {

    List<BankAccount> findByCustomerId(Long customerId);

    List<BankAccount> findByStatus(AccountStatus status);

    List<BankAccount> findByCustomerIdAndStatus(Long customerId, AccountStatus status);

    @Query("SELECT b FROM BankAccount b WHERE b.customer.id = :customerId AND b.status = 'ACTIVE'")
    List<BankAccount> findActiveAccountsByCustomerId(@Param("customerId") Long customerId);
}
