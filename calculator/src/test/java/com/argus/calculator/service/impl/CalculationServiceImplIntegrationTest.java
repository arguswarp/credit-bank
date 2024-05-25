package com.argus.calculator.service.impl;

import com.argus.calculator.dto.LoanOfferDto;
import com.argus.calculator.dto.LoanStatementRequestDto;
import com.argus.calculator.service.CalculationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Profile("test")
class CalculationServiceImplIntegrationTest {

    @Autowired
    private CalculationService calculationService;

    private final BigDecimal CALCULATED_MONTHLY_PAY_FIRST = BigDecimal.valueOf(3466.53);

    private final BigDecimal CALCULATED_MONTHLY_PAY_SECOND = BigDecimal.valueOf(3417.76);

    private final BigDecimal CALCULATED_MONTHLY_PAY_THIRD = BigDecimal.valueOf(3321.43);

    private final BigDecimal CALCULATED_MONTHLY_PAY_FOURTH = BigDecimal.valueOf(3273.87);

    @Test
    void generateLoanOffers() {
        List<LoanOfferDto> offers = calculationService.generateLoanOffers(LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(100_000))
                .term(3 * 12)
                .build());
        Assertions.assertNotNull(offers);
        Assertions.assertEquals(4, offers.size());

        Assertions.assertEquals(CALCULATED_MONTHLY_PAY_FIRST, offers.get(0).getMonthlyPayment());
        Assertions.assertEquals(CALCULATED_MONTHLY_PAY_SECOND, offers.get(1).getMonthlyPayment());
        Assertions.assertEquals(CALCULATED_MONTHLY_PAY_THIRD, offers.get(2).getMonthlyPayment());
        Assertions.assertEquals(CALCULATED_MONTHLY_PAY_FOURTH, offers.get(3).getMonthlyPayment());
    }
}