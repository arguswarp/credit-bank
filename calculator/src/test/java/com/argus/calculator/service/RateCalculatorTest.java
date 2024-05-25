package com.argus.calculator.service;

import com.argus.calculator.dto.EmploymentDto;
import com.argus.calculator.dto.ScoringDataDto;
import com.argus.calculator.model.enums.EmploymentStatus;
import com.argus.calculator.model.enums.MaritalStatus;
import com.argus.calculator.model.enums.Position;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RateCalculatorTest {

    private final BigDecimal BASE_RATE = BigDecimal.valueOf(15);

    private final BigDecimal INSURANCE_RATE_REDUCTION = BigDecimal.valueOf(3);

    private final BigDecimal CLIENT_RATE_REDUCTION = BigDecimal.valueOf(3);

    private final RateCalculator rateCalculator = new RateCalculator(BASE_RATE, INSURANCE_RATE_REDUCTION, CLIENT_RATE_REDUCTION);

    @Test
    void calculatePrescoringRate() {
        testRate(BASE_RATE, rateCalculator.calculatePrescoringRate(false, false));
        testRate(BASE_RATE.subtract(CLIENT_RATE_REDUCTION), rateCalculator.calculatePrescoringRate(false, true));
        testRate(BASE_RATE.subtract(INSURANCE_RATE_REDUCTION), rateCalculator.calculatePrescoringRate(true, false));
        testRate(BASE_RATE.subtract(CLIENT_RATE_REDUCTION)
                .subtract(INSURANCE_RATE_REDUCTION), rateCalculator.calculatePrescoringRate(true, true));
    }

    private void testRate(BigDecimal expected, BigDecimal rate) {
        assertNotNull(rate);
        assertEquals(expected, rate);
    }

    @Test
    void calculateScoringRate() {
        ScoringDataDto scoringDataDtoFirst = ScoringDataDto.builder()
                .employment(EmploymentDto.builder()
                        .position(Position.TOP_MANAGER)
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .build())
                .maritalStatus(MaritalStatus.MARRIED)
                .gender("male")
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .birthdate(LocalDate.now().minusYears(40))
                .build();
        BigDecimal rateFirst = rateCalculator.calculateScoringRate(scoringDataDtoFirst);
        assertNotNull(rateFirst);
        assertEquals(BASE_RATE.subtract(CLIENT_RATE_REDUCTION)
                        .subtract(INSURANCE_RATE_REDUCTION)
                        .subtract(BigDecimal.valueOf(3))
                        .subtract(BigDecimal.valueOf(3))
                        .subtract(BigDecimal.valueOf(3)),
                rateFirst);

        ScoringDataDto scoringDataDtoSecond = ScoringDataDto.builder()
                .employment(EmploymentDto.builder()
                        .position(Position.MANAGER)
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .build())
                .maritalStatus(MaritalStatus.DIVORCED)
                .gender("non binary")
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .birthdate(LocalDate.now().minusYears(28))
                .build();
        BigDecimal rateSecond = rateCalculator.calculateScoringRate(scoringDataDtoSecond);
        assertNotNull(rateFirst);
        assertEquals(BASE_RATE.subtract(BigDecimal.valueOf(2))
                        .add(BigDecimal.ONE)
                        .add(BigDecimal.ONE)
                        .add(BigDecimal.valueOf(7)),
                rateSecond);
    }
}