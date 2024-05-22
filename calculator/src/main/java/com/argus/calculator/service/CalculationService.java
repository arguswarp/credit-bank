package com.argus.calculator.service;

import com.argus.calculator.dto.CreditDto;
import com.argus.calculator.dto.LoanOfferDto;
import com.argus.calculator.dto.LoanStatementRequestDto;
import com.argus.calculator.dto.ScoringDataDto;

import java.util.List;

public interface CalculationService {

    List<LoanOfferDto> generateLoanOffers(LoanStatementRequestDto loanStatementRequestDto);

    CreditDto calculateCredit(ScoringDataDto scoringDataDto);
}
