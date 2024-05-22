package com.argus.calculator.service.impl;

import com.argus.calculator.dto.LoanOfferDto;
import com.argus.calculator.dto.LoanStatementRequestDto;
import com.argus.calculator.service.CalculationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
class CalculationServiceImplTest {

    @Autowired
    private CalculationService calculationService;

    @Test
    void generateLoanOffers() {
        List<LoanOfferDto> offers = calculationService.generateLoanOffers(LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(100_000))
                .term(3 * 12)
                .build());
        Assertions.assertNotNull(offers);
        Assertions.assertEquals(4, offers.size());
        Assertions.assertEquals(BigDecimal.valueOf(3226.72), offers.get(0).getMonthlyPayment());
    }

    @Test
    void calculateCredit() {
    }
}