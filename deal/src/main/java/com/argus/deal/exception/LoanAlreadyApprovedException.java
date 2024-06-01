package com.argus.deal.exception;

public class LoanAlreadyApprovedException extends RuntimeException {
    public LoanAlreadyApprovedException(String message) {
        super(message);
    }
}
