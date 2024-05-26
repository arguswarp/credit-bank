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

    private final BigDecimal INSURANCE_RATE_REDUCTION = BigDecimal.valueOf(-3);

    private final BigDecimal CLIENT_RATE_REDUCTION = BigDecimal.valueOf(-1);

    private final BigDecimal SELF_EMPLOYED_RATE_ADDITION = BigDecimal.valueOf(1);

    private final BigDecimal BUSINESS_OWNER_RATE_ADDITION = BigDecimal.valueOf(3);

    private final BigDecimal MANAGER_RATE_REDUCTION = BigDecimal.valueOf(-2);

    private final BigDecimal TOP_MANAGER_RATE_REDUCTION = BigDecimal.valueOf(-3);

    private final BigDecimal MARRIED_RATE_REDUCTION = BigDecimal.valueOf(-3);

    private final BigDecimal DIVORCED_RATE_ADDITION = BigDecimal.valueOf(1);

    private final BigDecimal MALE_RATE_REDUCTION = BigDecimal.valueOf(-3);

    private final BigDecimal FEMALE_RATE_REDUCTION = BigDecimal.valueOf(-3);

    private final BigDecimal NON_BINARY_RATE_ADDITION = BigDecimal.valueOf(7);

    private final RateCalculator rateCalculator = new RateCalculator(
            BASE_RATE,
            INSURANCE_RATE_REDUCTION,
            CLIENT_RATE_REDUCTION,
            SELF_EMPLOYED_RATE_ADDITION,
            BUSINESS_OWNER_RATE_ADDITION,
            MANAGER_RATE_REDUCTION,
            TOP_MANAGER_RATE_REDUCTION,
            MARRIED_RATE_REDUCTION,
            DIVORCED_RATE_ADDITION,
            MALE_RATE_REDUCTION,
            FEMALE_RATE_REDUCTION,
            NON_BINARY_RATE_ADDITION);

    @Test
    void calculatePrescoringRate() {
        testRate(BASE_RATE, rateCalculator.calculatePrescoringRate(false, false));
        testRate(BASE_RATE.add(CLIENT_RATE_REDUCTION), rateCalculator.calculatePrescoringRate(false, true));
        testRate(BASE_RATE.add(INSURANCE_RATE_REDUCTION), rateCalculator.calculatePrescoringRate(true, false));
        testRate(BASE_RATE.add(CLIENT_RATE_REDUCTION)
                .add(INSURANCE_RATE_REDUCTION), rateCalculator.calculatePrescoringRate(true, true));
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
        assertEquals(BASE_RATE.add(CLIENT_RATE_REDUCTION)
                        .add(INSURANCE_RATE_REDUCTION)
                        .add(TOP_MANAGER_RATE_REDUCTION)
                        .add(MARRIED_RATE_REDUCTION)
                        .add(MALE_RATE_REDUCTION),
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
        assertEquals(BASE_RATE.add(MANAGER_RATE_REDUCTION)
                        .add(SELF_EMPLOYED_RATE_ADDITION)
                        .add(DIVORCED_RATE_ADDITION)
                        .add(NON_BINARY_RATE_ADDITION),
                rateSecond);
    }
}