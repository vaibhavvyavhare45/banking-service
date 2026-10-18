package com.bank.banking_service.service;


import com.bank.banking_service.constant.AccountStatus;
import com.bank.banking_service.constant.TransactionEvent;
import com.bank.banking_service.constant.TransactionSate;
import com.bank.banking_service.entity.BankAccount;
import com.bank.banking_service.entity.Transaction;
import com.bank.banking_service.repository.BankAccountRepository;
import com.bank.banking_service.repository.TransactionRepository;
import com.bank.banking_service.state.TransactionStateMachine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionStateMachine transactionStateMachine;
    private final BankAccountRepository bankAccountRepository;

    public Transaction createTransaction(String fromAccount, String toAccount, BigDecimal amount) {
        Transaction txn = Transaction.builder().transactionId("TXN-" + UUID.randomUUID())
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(amount)
                .state(TransactionSate.INITIATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return transactionRepository.save(txn);

    }

    public Transaction processEvent(String transactionId, TransactionEvent transactionEvent) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("transaction not found" + transactionId));

        TransactionSate newState = transactionStateMachine.transition(transaction.getState(), transactionEvent);

        transaction.setState(newState);
        transaction.setUpdatedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction transferMoney(String fromAccount, String toAccount, BigDecimal amount) {

        BankAccount sender = bankAccountRepository.findById(fromAccount).orElseThrow(() -> new RuntimeException("sender account is not found "));
        BankAccount receiver = bankAccountRepository.findById(toAccount).orElseThrow(() -> new RuntimeException("receiver account is not found"));

        if (sender.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("sender account is not active");
        }
        if (receiver.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Receiver account is not active");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("amount must be greater than zero");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("balance is insufficient");
        }
        Transaction transaction = Transaction.builder()
                .transactionId("TXN_" + UUID.randomUUID())
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(amount)
                .state(TransactionSate.INITIATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        transaction=transactionRepository.save(transaction);

        transaction.setState(transactionStateMachine.transition(transaction.getState(),TransactionEvent.VALIDATE));
        transaction.setState(transactionStateMachine.transition(transaction.getState(),TransactionEvent.APPROVE));
        transaction.setState(transactionStateMachine.transition(transaction.getState(),TransactionEvent.PROCESS));

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));
        bankAccountRepository.save(sender);
        bankAccountRepository.save(receiver);
        //throw new RuntimeException("Testing rollback");
        transaction.setState(transactionStateMachine.transition(transaction.getState(),TransactionEvent.COMPLETE));
        transaction.setUpdatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }


}
