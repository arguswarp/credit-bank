package com.argus.calculator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static com.argus.calculator.util.CalculatorUtils.getMonthlyRate;
import static java.math.BigDecimal.ONE;
import static java.math.MathContext.DECIMAL32;
import static java.math.RoundingMode.HALF_EVEN;

@Component
public class CreditCalculator {

    @Value("${calculator.insurance.coefficient}")
    private BigDecimal INSURANCE_COEFFICIENT;

    private final MathContext MATH_CONTEXT = DECIMAL32;

    private final RoundingMode ROUNDING_MODE = HALF_EVEN;

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
     *
     * @param amount Сумма кредита
     * @param term   Срок кредита в месяцах
     * @param rate   Годовая процентная ставка кредита
     * @return Сумма ежемесячного платежа
     */
    public BigDecimal calculateMonthlyPayment(BigDecimal amount, int term, BigDecimal rate) {
        BigDecimal monthlyRate = getMonthlyRate(rate);
        //(1 + М) ^ S
        BigDecimal monthlyRateAddOnePowTerm = monthlyRate.add(ONE).pow(term);
        BigDecimal annuityCoefficient = monthlyRateAddOnePowTerm.multiply(monthlyRate)
                .divide(monthlyRateAddOnePowTerm.subtract(ONE), MATH_CONTEXT);
        return amount.multiply(annuityCoefficient).setScale(2, ROUNDING_MODE);
    }

    public BigDecimal calculateAmount(BigDecimal amount, boolean isInsuranceEnabled) {
        return amount.multiply(isInsuranceEnabled ? INSURANCE_COEFFICIENT : ONE);
    }
}
