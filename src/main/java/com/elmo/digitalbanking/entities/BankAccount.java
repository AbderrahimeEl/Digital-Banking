package com.elmo.digitalbanking.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class BankAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Double balance;
    private AccountStatus status;
    private String currency;
    private Long customerId;
    @OneToMany (mappedBy = "AccountOperation")
    private List<AccountOperation> operations;
}


