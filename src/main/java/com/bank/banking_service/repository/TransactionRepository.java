package com.bank.banking_service.repository;

import com.bank.banking_service.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction,String> {
}
