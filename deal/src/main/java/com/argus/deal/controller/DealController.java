package com.argus.deal.controller;

import com.argus.deal.dto.LoanOfferDto;
import com.argus.deal.dto.LoanStatementRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("deal")
public class DealController {

    @PostMapping("statement")
    public List<LoanOfferDto> sendOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        return List.of();
    }
}
