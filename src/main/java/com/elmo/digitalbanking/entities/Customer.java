package com.elmo.digitalbanking.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @OneToMany(mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<BankAccount> accountSet = new HashSet<>();
}

