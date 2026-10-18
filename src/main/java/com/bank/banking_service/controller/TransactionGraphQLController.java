package com.bank.banking_service.controller;

import com.bank.banking_service.constant.TransactionEvent;
import com.bank.banking_service.entity.Transaction;
import com.bank.banking_service.repository.TransactionRepository;
import com.bank.banking_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
@Controller
@RequiredArgsConstructor
public class TransactionGraphQLController {

        private final TransactionService transactionService;
        private final TransactionRepository transactionRepository;


        @MutationMapping
        public Transaction createTransaction(
                @Argument String fromAccount,
                @Argument String toAccount,
                @Argument BigDecimal amount) {

            return transactionService.createTransaction(
                    fromAccount,
                    toAccount,
                    amount
            );
        }

        @MutationMapping
        public Transaction transferMoney(
                @Argument String fromAccount,
                @Argument String toAccount,
                @Argument BigDecimal amount
        ){
            return transactionService.transferMoney(fromAccount, toAccount, amount);
        }

        @MutationMapping
        public Transaction processTransactionEvent(
                @Argument String transactionId,
                @Argument TransactionEvent event) {

            return transactionService.processEvent(
                    transactionId,
                    event
            );
        }

        @QueryMapping
        public Transaction transaction(
                @Argument String transactionId) {

            return transactionRepository
                    .findById(transactionId)
                    .orElse(null);
        }
}
