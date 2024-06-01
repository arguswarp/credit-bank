package com.argus.deal.controller;

import com.argus.deal.dto.LoanOfferDto;
import com.argus.deal.dto.LoanStatementRequestDto;
import com.argus.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("deal")
@RequiredArgsConstructor
@Slf4j
public class DealController {

    private final DealService dealService;

    @PostMapping("statement")
    @ResponseStatus(HttpStatus.OK)
    public List<LoanOfferDto> sendOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Received loan statement request: {}", loanStatementRequestDto);
        return dealService.getLoanOffers(loanStatementRequestDto);
    }

    @PostMapping("offer/select")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void selectOffer(@RequestBody LoanOfferDto loanOfferDto) {
        log.info("Received loan offer request: {}", loanOfferDto);
        dealService.selectOffer(loanOfferDto);
    }

}
