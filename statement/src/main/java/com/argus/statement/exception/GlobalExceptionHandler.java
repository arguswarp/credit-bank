package com.argus.statement.exception;


import com.argus.statement.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @ExceptionHandler(value = {DealApiException.class})
    public ResponseEntity<ErrorResponse> handleCalculatorExceptions(DealApiException e) throws IOException {
        ErrorResponse errorResponse = mapper.readValue(e.getBody(), ErrorResponse.class);
        log.error("Deal exception with status {} and message: {}", e.getStatus(), String.join(", ", errorResponse.getErrors()));
        return new ResponseEntity<>(errorResponse, e.getStatus());
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (e.getMostSpecificCause() instanceof DateTimeParseException) {
            log.error("DateTimeParseException", e);
            return new ResponseEntity<>(ErrorResponse.builder()
                    .errors(List.of("Неверный формат даты. Должен быть: yyyy.MM.dd"))
                    .build(), HttpStatus.BAD_REQUEST);
        }
        return super.handleHttpMessageNotReadable(e, headers, status, request);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(e.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .errors(e.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .errors(List.of("Server Error"))
                .build(), HttpStatus.BAD_REQUEST);
    }

}
