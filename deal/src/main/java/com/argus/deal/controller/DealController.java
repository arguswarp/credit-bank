package com.argus.deal.controller;

import com.argus.deal.dto.LoanOfferDto;
import com.argus.deal.dto.LoanStatementRequestDto;
import com.argus.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("deal")
@RequiredArgsConstructor
@Slf4j
public class DealController {

    private final DealService dealService;

    @PostMapping("statement")
    public List<LoanOfferDto> sendOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Received loan statement request: {}", loanStatementRequestDto);
        return dealService.getLoanOffers(loanStatementRequestDto);
    }

}
