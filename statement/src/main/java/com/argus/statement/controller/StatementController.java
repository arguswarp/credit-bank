package com.argus.statement.controller;

import com.argus.statement.dto.LoanOfferDto;
import com.argus.statement.dto.LoanStatementRequestDto;
import com.argus.statement.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * StatementController
 *
 * @author Maxim Chistyakov
 */
@RestController
@RequestMapping("statement")
@RequiredArgsConstructor
public class StatementController {

    private final StatementService statementService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LoanOfferDto> sendOffers(@Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        return statementService.getLoanOffers(loanStatementRequestDto);
    }

    @PostMapping("offer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void selectOffer(@RequestBody LoanOfferDto loanOfferDto) {
        statementService.selectOffer(loanOfferDto);
    }
}
