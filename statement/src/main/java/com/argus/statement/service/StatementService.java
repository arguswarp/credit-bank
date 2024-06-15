package com.argus.statement.service;

import com.argus.statement.dto.LoanOfferDto;
import com.argus.statement.dto.LoanStatementRequestDto;
import com.argus.statement.exception.DealApiException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
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
public class StatementService {

    private final DealFeignClient dealFeignClient;

    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        try {
            return dealFeignClient.getLoanOffers(loanStatementRequestDto);
        } catch (FeignException e) {
            throw new DealApiException(e.responseBody().get().array(), HttpStatus.valueOf(e.status()));
        }
    }

    public void selectOffer(LoanOfferDto loanOfferDto) {
        try {
            dealFeignClient.selectOffer(loanOfferDto);
        } catch (FeignException e) {
            throw new DealApiException(e.responseBody().get().array(), HttpStatus.valueOf(e.status()));
        }
    }
}
