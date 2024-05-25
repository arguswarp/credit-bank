package com.argus.calculator.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ClientDeniedException.class})
    public ResponseEntity<Object> handleClientDeniedException(ClientDeniedException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (e.getMostSpecificCause() instanceof DateTimeParseException) {
            log.error("DateTimeParseException", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Неверный формат даты. Должен быть: yyyy.MM.dd"));
        }
        return super.handleHttpMessageNotReadable(e, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest()
                .body(
                        Map.of("errors",
                                e.getFieldErrors().stream()
                                        .map(DefaultMessageSourceResolvable::getDefaultMessage).toList())
                );
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage());
        return ResponseEntity.internalServerError().build();
    }

}
