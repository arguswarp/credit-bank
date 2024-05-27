package com.argus.calculator.exception;

import com.argus.calculator.dto.ErrorResponse;
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

import java.time.format.DateTimeParseException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ClientDeniedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleClientDeniedException(ClientDeniedException e) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errors(List.of(e.getMessage()))
                .build(), HttpStatus.BAD_REQUEST);
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
