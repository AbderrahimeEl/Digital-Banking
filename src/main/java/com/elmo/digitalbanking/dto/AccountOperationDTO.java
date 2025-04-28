package com.elmo.digitalbanking.dto;

import com.elmo.digitalbanking.entities.OperationType;
import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountOperationDTO {
    private Long id;
    private Date date;
    private double amount;
    private OperationType type;
    private Long bankAccountId;
}
