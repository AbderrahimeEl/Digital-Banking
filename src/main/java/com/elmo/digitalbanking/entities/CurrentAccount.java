package com.elmo.digitalbanking.entities;


import jakarta.persistence.Entity;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder

public class CurrentAccount extends BankAccount{
    private Double overDraft;
}
