package com.argus.deal.exception;

/**
 * StatementNotExistException.
 *
 * @author Maxim Chistyakov
 */
public class StatementNotExistException extends RuntimeException {
    public StatementNotExistException(String message) {
        super(message);
    }
}
