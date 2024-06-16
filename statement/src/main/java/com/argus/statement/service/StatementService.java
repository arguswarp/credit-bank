package com.argus.statement.service;

import com.argus.statement.dto.LoanOfferDto;
import com.argus.statement.dto.LoanStatementRequestDto;
import com.argus.statement.exception.DealApiException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * StatementService
 *
 * @author Maxim Chistyakov
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StatementService {

    private final DealFeignClient dealFeignClient;

    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        try {
            log.info("Executing POST /deal/statement: ");
            return dealFeignClient.getLoanOffers(loanStatementRequestDto);
        } catch (FeignException e) {
            throw new DealApiException(e.responseBody().orElseThrow(()-> new RuntimeException("Empty error response from Deal")).array(), HttpStatus.valueOf(e.status()));
        }
    }

    public void selectOffer(LoanOfferDto loanOfferDto) {
        try {
            log.info("Executing POST /deal/offer/select");
            dealFeignClient.selectOffer(loanOfferDto);
        } catch (FeignException e) {
            throw new DealApiException(e.responseBody().orElseThrow(()-> new RuntimeException("Empty error response from Deal")).array(), HttpStatus.valueOf(e.status()));
        }
    }
}
