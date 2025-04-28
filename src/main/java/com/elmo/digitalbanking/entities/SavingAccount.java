package com.elmo.digitalbanking.entities;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder

public class SavingAccount extends BankAccount{
    private Double interestRate;
}
