package com.argus.deal.exception;


import com.argus.deal.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @ExceptionHandler(value = {StatementNotExistException.class, LoanAlreadyApprovedException.class, CreditAlreadyCalculatedException.class, InconsistentDataException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleDealExceptions(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .errors(List.of(e.getMessage()))
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {CalculatorApiException.class})
    public ResponseEntity<ErrorResponse> handleCalculatorExceptions(CalculatorApiException e) throws IOException {
        ErrorResponse errorResponse = mapper.readValue(e.getBody(), ErrorResponse.class);
        log.error("Calculator Exception with status {} and message: {}", e.getStatus(), String.join(", ", errorResponse.getErrors()));
        return new ResponseEntity<>(errorResponse, e.getStatus());
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(ErrorResponse.builder()
                .errors(List.of("Server Error"))
                .build(), HttpStatus.BAD_REQUEST);
    }

}
