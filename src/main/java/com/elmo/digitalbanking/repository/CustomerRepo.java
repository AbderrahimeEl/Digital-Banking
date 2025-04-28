package com.elmo.digitalbanking.repository;

import com.elmo.digitalbanking.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
}
