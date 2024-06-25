package com.argus.statement.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * DealApiException
 *
 * @author Maxim Chistyakov
 */
@RequiredArgsConstructor
@Getter
public class DealApiException extends RuntimeException {

    private final byte[] body;

    private final HttpStatus status;

}
