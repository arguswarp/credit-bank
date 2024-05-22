package com.argus.calculator.controller;

import com.argus.calculator.dto.CreditDto;
import com.argus.calculator.dto.LoanOfferDto;
import com.argus.calculator.dto.LoanStatementRequestDto;
import com.argus.calculator.dto.ScoringDataDto;
import com.argus.calculator.service.CalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("calculator")
public class CalculatorController {

    private final CalculationService calculationService;

    @PostMapping("offers")
    @ResponseStatus(HttpStatus.OK)
    public List<LoanOfferDto> sendOffers(@Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        return calculationService.generateLoanOffers(loanStatementRequestDto);
    }

    @PostMapping("calc")
    @ResponseStatus(HttpStatus.OK)
    public CreditDto sendCredit(@Valid @RequestBody ScoringDataDto scoringDataDto) {
        return calculationService.calculateCredit(scoringDataDto);
    }
}
