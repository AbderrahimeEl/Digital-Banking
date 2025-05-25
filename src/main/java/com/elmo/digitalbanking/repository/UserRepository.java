package com.elmo.digitalbanking.repository;

import com.elmo.digitalbanking.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<User> findByCustomerId(Long customerId);
}
