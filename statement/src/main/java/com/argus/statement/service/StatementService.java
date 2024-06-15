package com.argus.statement.service;

import com.argus.statement.dto.LoanOfferDto;
import com.argus.statement.dto.LoanStatementRequestDto;
import lombok.RequiredArgsConstructor;
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
        return dealFeignClient.getLoanOffers(loanStatementRequestDto);
    }

    public void selectOffer(LoanOfferDto loanOfferDto) {
        dealFeignClient.selectOffer(loanOfferDto);
    }
}
