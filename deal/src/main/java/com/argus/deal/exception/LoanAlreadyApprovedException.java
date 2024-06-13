package com.argus.deal.exception;

/**
 * LoanAlreadyApprovedException.
 *
 * @author Maxim Chistyakov
 */
public class LoanAlreadyApprovedException extends RuntimeException {
    public LoanAlreadyApprovedException(String message) {
        super(message);
    }
}
