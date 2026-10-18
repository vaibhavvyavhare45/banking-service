package com.bank.banking_service.entity;

import com.bank.banking_service.constant.TransactionSate;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String transactionId;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private TransactionSate state;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
