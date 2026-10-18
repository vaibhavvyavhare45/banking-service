package com.bank.banking_service.entity;

import com.bank.banking_service.constant.AccountStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document(collection = "bank_accounts")
public class BankAccount {
@Id
private String accountNumber;
private String customerId;
private String accountType;
private BigDecimal balance;
private String currency;
private AccountStatus accountStatus;



}
