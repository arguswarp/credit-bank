package com.argus.deal.exception;

/**
 * CreditAlreadyCalculatedException.
 *
 * @author Maxim Chistyakov
 */
public class CreditAlreadyCalculatedException extends RuntimeException {
    public CreditAlreadyCalculatedException(String message) {
        super(message);
    }
}
