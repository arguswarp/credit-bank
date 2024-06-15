package com.argus.statement.controller;

import com.argus.statement.dto.LoanOfferDto;
import com.argus.statement.dto.LoanStatementRequestDto;
import com.argus.statement.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<LoanOfferDto> sendOffers(@Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        return statementService.getLoanOffers(loanStatementRequestDto);
    }

    @PostMapping("offer")
    public void selectOffer(@RequestBody LoanOfferDto loanOfferDto) {
        statementService.selectOffer(loanOfferDto);
    }
}
