package com.elmo.digitalbanking.entities;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder

public class SavingAccount extends BankAccount{
    private Double interestRate;
}
