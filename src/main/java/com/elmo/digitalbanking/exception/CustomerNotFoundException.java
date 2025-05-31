package com.elmo.digitalbanking.exception;

public class CustomerNotFoundException extends BankingException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
