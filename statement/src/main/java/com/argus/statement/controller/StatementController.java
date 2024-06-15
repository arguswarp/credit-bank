package com.argus.statement.controller;

import com.argus.statement.dto.LoanOfferDto;
import com.argus.statement.dto.LoanStatementRequestDto;
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
public class StatementController {

    @PostMapping
    public List<LoanOfferDto> sendOffers(@Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        return List.of();
    }

    @PostMapping("offer")
    public void selectOffer(@Valid @RequestBody LoanOfferDto loanOfferDto) {

    }
}
