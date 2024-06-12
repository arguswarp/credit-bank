package com.argus.deal.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * CalculatorApiException.
 *
 * @author Maxim Chistyakov
 */
@RequiredArgsConstructor
@Getter
public class CalculatorApiException extends RuntimeException {

    private final byte[] body;

    private final HttpStatus status;
}
