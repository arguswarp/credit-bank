package com.argus.calculator.service.impl;

import com.argus.calculator.dto.CreditDto;
import com.argus.calculator.dto.LoanOfferDto;
import com.argus.calculator.dto.LoanStatementRequestDto;
import com.argus.calculator.dto.ScoringDataDto;
import com.argus.calculator.service.CalculationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL32;
import static java.math.RoundingMode.HALF_EVEN;

@Service
public class CalculationServiceImpl implements CalculationService {

    @Value("${calculator.base-rate}")
    private BigDecimal BASE_RATE;

    @Value("${calculator.insurance.rate-reduction}")
    private BigDecimal INSURANCE_RATE_REDUCTION;

    @Value("${calculator.client.rate-reduction}")
    private BigDecimal CLIENT_RATE_REDUCTION;

    @Value("${calculator.insurance.coefficient}")
    private BigDecimal INSURANCE_COEFFICIENT;

    private final MathContext MATH_CONTEXT = DECIMAL32;

    private final RoundingMode ROUNDING_MODE = HALF_EVEN;

    @Override
    public List<LoanOfferDto> generateLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return List.of(
                generateOffer(loanStatementRequestDto, false, false),
                generateOffer(loanStatementRequestDto, false, true),
                generateOffer(loanStatementRequestDto, true, false),
                generateOffer(loanStatementRequestDto, true, true)
        );
    }

    private LoanOfferDto generateOffer(LoanStatementRequestDto loanStatementRequest, boolean isInsuranceEnabled, boolean isSalaryClient) {
        BigDecimal amount = loanStatementRequest.getAmount();
        BigDecimal rate = calculatePrescoringRate(isInsuranceEnabled, isSalaryClient);
        int term = loanStatementRequest.getTerm();
        return LoanOfferDto.builder()
                .requestedAmount(amount)
                .totalAmount(calculatePrescoringAmount(amount, isInsuranceEnabled))
                .term(term)
                .monthlyPayment(calculatePrescoringMonthlyPayment(amount, term, rate))
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
    }

    @Override
    public CreditDto calculateCredit(ScoringDataDto scoringDataDto) {
        return null;
    }

    /**
     * Используется формула расчета аннуитетного платежа:
     * <p>
     * Х = С * К
     * <p>
     * где X — аннуитетный платеж,
     * С — сумма кредита,
     * К — коэффициент аннуитета.
     * <p>
     * Коэффициент аннуитета считается так:
     * <p>
     * К = (М * (1 + М) ^ S) / ((1 + М) ^ S — 1)
     * <p>
     * где М — месячная процентная ставка по кредиту,
     * S — срок кредита в месяцах.
     * @param amount Сумма кредита
     * @param term Срок кредита в месяцах
     * @param rate Годовая процентная ставка кредита
     * @return Сумма ежемесячного платежа
     */
    private BigDecimal calculatePrescoringMonthlyPayment(BigDecimal amount, int term, BigDecimal rate) {
        //TODO rename
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100), MATH_CONTEXT)
                .divide(BigDecimal.valueOf(12), MATH_CONTEXT);
        //(1 + М) ^ S
        BigDecimal equationPart = monthlyRate.add(ONE).pow(term);
        BigDecimal annuityCoefficient = equationPart.multiply(monthlyRate)
                .divide(equationPart.subtract(ONE), MATH_CONTEXT);
        return amount.multiply(annuityCoefficient).setScale(2, ROUNDING_MODE);
    }

    private BigDecimal calculatePrescoringRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        return BASE_RATE.subtract(isInsuranceEnabled ? INSURANCE_RATE_REDUCTION : ZERO)
                .subtract(isSalaryClient ? CLIENT_RATE_REDUCTION : ZERO);
    }

    private BigDecimal calculatePrescoringAmount(BigDecimal amount,boolean isInsuranceEnabled) {
        return amount.multiply(isInsuranceEnabled ? INSURANCE_COEFFICIENT : ONE);
    }
}
