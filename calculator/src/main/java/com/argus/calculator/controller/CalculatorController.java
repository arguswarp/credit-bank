package com.argus.calculator.controller;

import com.argus.calculator.dto.*;
import com.argus.calculator.service.CalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("calculator")
@Slf4j
public class CalculatorController {

    private final CalculationService calculationService;

    @Operation(summary = "Выполняет прескоринг и отправляет четыре кредитных предложения")
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    @PostMapping(value = "offers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<LoanOfferDto> sendOffers(@Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Sending offer request: {}", loanStatementRequestDto);
        return calculationService.generateLoanOffers(loanStatementRequestDto);
    }

    @Operation(summary = "Рассчитывает кредит и отправляет график платежей")
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    @PostMapping(value = "calc", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CreditDto sendCredit(@Valid @RequestBody ScoringDataDto scoringDataDto) {
        log.info("Sending credit request: {}", scoringDataDto);
        return calculationService.calculateCredit(scoringDataDto);
    }
}
