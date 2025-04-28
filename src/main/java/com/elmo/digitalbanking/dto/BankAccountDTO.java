package com.elmo.digitalbanking.dto;

import com.elmo.digitalbanking.entities.AccountStatus;
import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountDTO {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Double balance;
    private AccountStatus status;
    private String currency;
    private Long customerId;
    private Double overDraft;
    private Double interestRate;
}