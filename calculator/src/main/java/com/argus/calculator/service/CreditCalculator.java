package com.argus.calculator.service;

import com.argus.calculator.dto.PaymentScheduleElementDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.argus.calculator.util.CalculatorUtils.getMonthlyRate;
import static java.math.BigDecimal.ONE;
import static java.math.MathContext.DECIMAL64;
import static java.math.RoundingMode.HALF_EVEN;

@AllArgsConstructor
@Component
@Slf4j
public class CreditCalculator {

    @Value("${calculator.insurance.coefficient}")
    private BigDecimal INSURANCE_COEFFICIENT;

    private final MathContext MATH_CONTEXT = DECIMAL64;

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
        log.info("Calculating monthly payment");
        BigDecimal monthlyRate = getMonthlyRate(rate);
        //(1 + М) ^ S
        BigDecimal monthlyRateAddOnePowTerm = monthlyRate.add(ONE).pow(term);
        BigDecimal annuityCoefficient = monthlyRateAddOnePowTerm.multiply(monthlyRate)
                .divide(monthlyRateAddOnePowTerm.subtract(ONE), MATH_CONTEXT);
        return amount.multiply(annuityCoefficient);
    }

    public BigDecimal calculateMonthlyPayment(BigDecimal amount, int term, BigDecimal rate, RoundingMode roundingMode) {
        log.info("Calculating monthly payment with rounding mode {}", roundingMode);
        return calculateMonthlyPayment(amount, term, rate).setScale(2, roundingMode);
    }

    public BigDecimal calculateAmount(BigDecimal amount, boolean isInsuranceEnabled) {
        log.info("Calculating amount");
        return amount.multiply(isInsuranceEnabled ? INSURANCE_COEFFICIENT : ONE).setScale(2, ROUNDING_MODE);
    }

    public BigDecimal calculatePSK(BigDecimal montlyPayment, int term) {
        log.info("Calculating PSK");
        return montlyPayment.multiply(BigDecimal.valueOf(term)).setScale(2, ROUNDING_MODE);
    }

    public List<PaymentScheduleElementDto> calculatePaymentSchedule(BigDecimal amount, int term, BigDecimal monthlyPayment, BigDecimal rate) {
        log.info("Calculating payment schedule");
        List<PaymentScheduleElementDto> paymentSchedule = new ArrayList<>();
        BigDecimal monthlyRate = getMonthlyRate(rate);
        LocalDate date = LocalDate.now().plusMonths(1);
        BigDecimal remainDebt = amount;
        for (int i = 1; i <= term; i++) {
            PaymentScheduleElementDto scheduleElement = calculatePaymentScheduleElement(i, date, monthlyPayment, remainDebt, monthlyRate);
            paymentSchedule.add(scheduleElement);

            date = date.plusMonths(1);
            remainDebt = scheduleElement.getRemainDebt();

            round(scheduleElement);
        }
        return paymentSchedule;
    }

    private PaymentScheduleElementDto calculatePaymentScheduleElement(int number, LocalDate date, BigDecimal monthlyPayment, BigDecimal debt, BigDecimal monthlyRate) {
        BigDecimal interestPayment = debt.multiply(monthlyRate, MATH_CONTEXT);
        BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
        BigDecimal remainDebt = debt.subtract(debtPayment);
        return PaymentScheduleElementDto.builder()
                .number(number)
                .date(date)
                .totalPayment(monthlyPayment)
                .interestPayment(interestPayment)
                .debtPayment(debtPayment)
                .remainDebt(remainDebt)
                .build();
    }

    private void round(PaymentScheduleElementDto paymentScheduleElementDto) {
        BigDecimal monthlyPayment = paymentScheduleElementDto.getTotalPayment();
        BigDecimal interestPayment = paymentScheduleElementDto.getInterestPayment();
        BigDecimal debtPayment = paymentScheduleElementDto.getDebtPayment();
        BigDecimal remainDebt = paymentScheduleElementDto.getRemainDebt();
        paymentScheduleElementDto.setTotalPayment(monthlyPayment.setScale(2, ROUNDING_MODE));
        paymentScheduleElementDto.setInterestPayment(interestPayment.setScale(2, ROUNDING_MODE));
        paymentScheduleElementDto.setDebtPayment(debtPayment.setScale(2, ROUNDING_MODE));
        paymentScheduleElementDto.setRemainDebt(remainDebt.setScale(2, ROUNDING_MODE));
    }
}
