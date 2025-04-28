package com.elmo.digitalbanking.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @SuperBuilder
public class BankAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private Double balance;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private String currency;

    @OneToMany(mappedBy = "bankAccount",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<AccountOperation> operations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}



