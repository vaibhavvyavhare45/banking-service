package com.bank.banking_service.state;

import com.bank.banking_service.constant.TransactionEvent;
import com.bank.banking_service.constant.TransactionSate;
import org.springframework.stereotype.Component;

@Component
public class TransactionStateMachine {
    public TransactionSate transition(TransactionSate currentSate, TransactionEvent event){

        return switch (currentSate){
            case INITIATED -> {
                if (event==TransactionEvent.VALIDATE){
                   yield TransactionSate.VALIDATING;
                }
                throw inValidTransition(currentSate,event);
            }
            case VALIDATING -> {
                if (event==TransactionEvent.APPROVE){
                    yield TransactionSate.APPROVED;
                }
                if (event==TransactionEvent.REJECT){
                    yield  TransactionSate.REJECTED;
                }
                throw inValidTransition(currentSate,event);
            }
            case APPROVED -> {
                if (event==TransactionEvent.PROCESS){
                    yield TransactionSate.PROCESSING;
                }
                if (event==TransactionEvent.CANCEL){
                    yield  TransactionSate.CANCELLED;
                }
                throw  inValidTransition(currentSate,event);
            }
            case PROCESSING -> {
                if (event==TransactionEvent.COMPLETE){
                    yield TransactionSate.COMPLETED;
                }
                if (event==TransactionEvent.FAIL){
                    yield TransactionSate.FAILED;
                }
                throw inValidTransition(currentSate,event);
            }
            case COMPLETED, REJECTED , FAILED , CANCELLED -> throw  new IllegalStateException("transaction already in final state: "+ currentSate);
        };

    }

    private IllegalStateException inValidTransition(TransactionSate currentSate, TransactionEvent event) {
    return new IllegalStateException("Invalid Transition state: "+currentSate +", "+ event);
    }
}
