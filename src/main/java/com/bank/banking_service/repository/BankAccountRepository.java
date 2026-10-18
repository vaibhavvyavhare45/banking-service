package com.bank.banking_service.repository;

import com.bank.banking_service.entity.BankAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends MongoRepository<BankAccount,String> {

}
