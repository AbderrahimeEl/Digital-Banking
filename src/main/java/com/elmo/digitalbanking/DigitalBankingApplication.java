package com.elmo.digitalbanking;

import com.elmo.digitalbanking.entities.*;
import com.elmo.digitalbanking.repository.AccountOperationRepo;
import com.elmo.digitalbanking.repository.BankAccountRepo;
import com.elmo.digitalbanking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.elmo.digitalbanking.entities.AccountStatus.ACTIVE;

@SpringBootApplication
public class DigitalBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }
    
    @Bean
    public CommandLineRunner initData(BankAccountRepo bankAccountRepo,
                                   AccountOperationRepo accountOperationRepo,
                                   UserRepository userRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin user if not exists
            if (!userRepository.existsByUsername("admin")) {
                User adminUser = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role(Role.ROLE_ADMIN)
                        .build();
                userRepository.save(adminUser);
                System.out.println("Admin user created with username: admin, password: admin");
            }
            
            BankAccount account = bankAccountRepo.save(
                    BankAccount.builder()
                            .createdAt(new Date())
                            .updatedAt(new Date())
                            .balance(10_000.0)
                            .status(ACTIVE)
                            .currency("USD")
                            .build()
            );

            AccountOperation op1 = AccountOperation.builder()
                    .date(new Date())
                    .amount(500.0)
                    .type(OperationType.CREDIT)
                    .bankAccount(account)
                    .build();
            AccountOperation op2 = AccountOperation.builder()
                    .date(new Date())
                    .amount(200.0)
                    .type(OperationType.DEBIT)
                    .bankAccount(account)
                    .build();

            accountOperationRepo.saveAll(List.of(op1, op2));

        };
    }
}