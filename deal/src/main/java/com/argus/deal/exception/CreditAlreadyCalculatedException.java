package com.argus.deal.exception;

public class CreditAlreadyCalculatedException extends RuntimeException {
    public CreditAlreadyCalculatedException(String message) {
        super(message);
    }

    public CreditAlreadyCalculatedException() {
    }
}
