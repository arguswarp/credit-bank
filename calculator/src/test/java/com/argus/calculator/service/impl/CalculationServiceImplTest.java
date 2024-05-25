package com.argus.calculator.service.impl;

import com.argus.calculator.dto.*;
import com.argus.calculator.exception.ClientDeniedException;
import com.argus.calculator.model.enums.EmploymentStatus;
import com.argus.calculator.service.CreditCalculator;
import com.argus.calculator.service.RateCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CalculationServiceImplTest {

    private final BigDecimal AMOUNT = BigDecimal.valueOf(20_000);

    private final int TERM = 36;

    private final BigDecimal RATE = BigDecimal.valueOf(12);

    private final BigDecimal MONTHLY_PAYMENT = BigDecimal.valueOf(664.29);

    @InjectMocks
    private CalculationServiceImpl calculationService;

    @Mock
    private RateCalculator mockRateCalculator;

    @Mock
    private CreditCalculator mockCreditCalculator;

    @Test
    void generateLoanOffers() {
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(AMOUNT)
                .term(TERM)
                .build();

        Mockito.when(mockRateCalculator.calculatePrescoringRate(Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(RATE);
        Mockito.when(mockCreditCalculator.calculateAmount(Mockito.any(), Mockito.anyBoolean())).thenReturn(AMOUNT);
        Mockito.when(mockCreditCalculator.calculateMonthlyPayment(Mockito.any(), Mockito.anyInt(), Mockito.any(), Mockito.any())).thenReturn(MONTHLY_PAYMENT);

        List<LoanOfferDto> loanOffers = calculationService.generateLoanOffers(loanStatementRequestDto);

        Mockito.verify(mockRateCalculator, Mockito.times(4)).calculatePrescoringRate(Mockito.anyBoolean(), Mockito.anyBoolean());
        Mockito.verify(mockCreditCalculator, Mockito.times(4)).calculateAmount(Mockito.eq(AMOUNT), Mockito.anyBoolean());
        Mockito.verify(mockCreditCalculator, Mockito.times(4)).calculateMonthlyPayment(AMOUNT, TERM, RATE, RoundingMode.HALF_EVEN);

        assertEquals(4, loanOffers.size());
        loanOffers.forEach(offer -> {
            assertNotNull(offer);
            assertNotNull(offer.getTotalAmount());
            assertNotNull(offer.getMonthlyPayment());
            assertNotNull(offer.getRate());
        });
    }

    @Test
    void WhenDataForDenialIsGiven_ThenThrowClientDeniedException() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.UNEMPLOYED)
                        .build())
                .build();

        assertThrows(ClientDeniedException.class, () -> calculationService.calculateCredit(scoringDataDto));

        ScoringDataDto scoringDataDtoSecond = ScoringDataDto.builder()
                .amount(AMOUNT)
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .salary(BigDecimal.ONE)
                        .build())
                .build();

        assertThrows(ClientDeniedException.class, () -> calculationService.calculateCredit(scoringDataDtoSecond));

        ScoringDataDto scoringDataDtoThird = ScoringDataDto.builder()
                .amount(AMOUNT)
                .birthdate(LocalDate.now().minusYears(90))
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .salary(AMOUNT.multiply(BigDecimal.TEN))
                        .build())
                .build();

        assertThrows(ClientDeniedException.class, () -> calculationService.calculateCredit(scoringDataDtoThird));

        ScoringDataDto scoringDataDtoFourth = ScoringDataDto.builder()
                .amount(AMOUNT)
                .birthdate(LocalDate.now().minusYears(35))
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .salary(AMOUNT.multiply(BigDecimal.TEN))
                        .workExperienceTotal(1)
                        .build())
                .build();

        assertThrows(ClientDeniedException.class, () -> calculationService.calculateCredit(scoringDataDtoFourth));

        ScoringDataDto scoringDataDtoFifth = ScoringDataDto.builder()
                .amount(AMOUNT)
                .birthdate(LocalDate.now().minusYears(35))
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .salary(AMOUNT.multiply(BigDecimal.TEN))
                        .workExperienceTotal(100)
                        .workExperienceCurrent(1)
                        .build())
                .build();

        assertThrows(ClientDeniedException.class, () -> calculationService.calculateCredit(scoringDataDtoFifth));
    }

    @Test
    void calculateCredit() {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .amount(AMOUNT)
                .term(TERM)
                .birthdate(LocalDate.now().minusYears(35))
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .salary(AMOUNT.multiply(BigDecimal.TEN))
                        .workExperienceTotal(100)
                        .workExperienceCurrent(10)
                        .build())
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        Mockito.when(mockCreditCalculator.calculateAmount(Mockito.any(), Mockito.anyBoolean())).thenReturn(AMOUNT);
        Mockito.when(mockRateCalculator.calculateScoringRate(Mockito.any(ScoringDataDto.class))).thenReturn(RATE);
        Mockito.when(mockCreditCalculator.calculateMonthlyPayment(Mockito.any(), Mockito.anyInt(), Mockito.any())).thenReturn(MONTHLY_PAYMENT);
        Mockito.when(mockCreditCalculator.calculatePSK(Mockito.any(), Mockito.anyInt())).thenReturn(Mockito.mock(BigDecimal.class));
        Mockito.when(mockCreditCalculator.calculatePaymentSchedule(Mockito.any(), Mockito.anyInt(), Mockito.any(), Mockito.any())).thenReturn(List.of(PaymentScheduleElementDto.builder().build()));

        CreditDto creditDto = calculationService.calculateCredit(scoringDataDto);

        assertNotNull(creditDto);

        Mockito.verify(mockRateCalculator).calculateScoringRate(scoringDataDto);
        Mockito.verify(mockCreditCalculator).calculateAmount(Mockito.eq(AMOUNT), Mockito.anyBoolean());
        Mockito.verify(mockCreditCalculator).calculateMonthlyPayment(AMOUNT, TERM, RATE);
        Mockito.verify(mockCreditCalculator).calculatePSK(MONTHLY_PAYMENT, TERM);
        Mockito.verify(mockCreditCalculator).calculatePaymentSchedule(AMOUNT, TERM, MONTHLY_PAYMENT, RATE);

        assertNotNull(creditDto.getAmount());
        assertNotNull(creditDto.getRate());
        assertNotNull(creditDto.getTerm());
        assertNotNull(creditDto.getMonthlyPayment());
        assertNotNull(creditDto.getRate());
        assertNotNull(creditDto.getIsInsuranceEnabled());
        assertNotNull(creditDto.getIsSalaryClient());
        assertEquals(1, creditDto.getPaymentSchedule().size());
    }
}