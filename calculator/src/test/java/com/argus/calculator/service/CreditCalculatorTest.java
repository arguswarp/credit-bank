package com.argus.calculator.service;

import com.argus.calculator.dto.PaymentScheduleElementDto;
import com.argus.calculator.util.CalculatorUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreditCalculatorTest {

    private final BigDecimal INSURANCE_COEFFICIENT = BigDecimal.valueOf(1.1);

    private final BigDecimal RATE_20_000 = BigDecimal.valueOf(12);

    private final BigDecimal RATE_5_000_000 = BigDecimal.valueOf(5);

    private final BigDecimal AMOUNT_20_000 = BigDecimal.valueOf(20_000);

    private final BigDecimal AMOUNT_5_000_000 = BigDecimal.valueOf(5_000_000);

    private final BigDecimal MONTHLY_PAY_FOR_20_000 = BigDecimal.valueOf(664.2861963);

    private final BigDecimal MONTHLY_PAY_FOR_5_000_000 = BigDecimal.valueOf(22706.93845);

    private final CreditCalculator creditCalculator = new CreditCalculator(INSURANCE_COEFFICIENT);

    @Test
    void calculateMonthlyPayment() {
        BigDecimal monthlyPaymentFirst = creditCalculator.calculateMonthlyPayment(AMOUNT_20_000, 36, RATE_20_000)
                .setScale(2, RoundingMode.HALF_EVEN);
        testBigDecimal(MONTHLY_PAY_FOR_20_000.setScale(2, RoundingMode.HALF_EVEN), monthlyPaymentFirst);

        BigDecimal monthlyPaymentSecond = creditCalculator.calculateMonthlyPayment(BigDecimal.valueOf(5_000_000), 600, RATE_5_000_000)
                .setScale(2, RoundingMode.HALF_EVEN);
        testBigDecimal(MONTHLY_PAY_FOR_5_000_000.setScale(2,RoundingMode.HALF_EVEN), monthlyPaymentSecond);
    }

    private void testBigDecimal(BigDecimal expected, BigDecimal monthlyPayment) {
        assertNotNull(monthlyPayment);
        assertEquals(expected, monthlyPayment);
    }

    @Test
    void calculateAmount() {
        BigDecimal amountFirst = creditCalculator.calculateAmount(AMOUNT_20_000, true);
        testBigDecimal(AMOUNT_20_000.multiply(INSURANCE_COEFFICIENT).setScale(2, RoundingMode.HALF_EVEN), amountFirst);

        BigDecimal amountSecond = creditCalculator.calculateAmount(AMOUNT_5_000_000, true);
        testBigDecimal(AMOUNT_5_000_000.multiply(INSURANCE_COEFFICIENT).setScale(2, RoundingMode.HALF_EVEN), amountSecond);
    }

    @Test
    void calculatePSK() {
        BigDecimal pskFirst = creditCalculator.calculatePSK(MONTHLY_PAY_FOR_20_000, 36);
        testBigDecimal(BigDecimal.valueOf(23914.30).setScale(2, RoundingMode.HALF_EVEN), pskFirst);

        BigDecimal pskSecond = creditCalculator.calculatePSK(MONTHLY_PAY_FOR_5_000_000, 600);
        testBigDecimal(BigDecimal.valueOf(13624163.07).setScale(2), pskSecond);
    }

    @Test
    void calculatePaymentSchedule() {
        List<PaymentScheduleElementDto> paymentScheduleElementDto = creditCalculator.calculatePaymentSchedule(AMOUNT_20_000, 36, MONTHLY_PAY_FOR_20_000, RATE_20_000);
        BigDecimal monthlyRate = CalculatorUtils.getMonthlyRate(RATE_20_000);
        AtomicReference<BigDecimal> amount = new AtomicReference<>(AMOUNT_20_000);
        paymentScheduleElementDto.forEach(element -> {
            assertNotNull(element);
            assertEquals(amount.get().multiply(monthlyRate).setScale(2, RoundingMode.HALF_EVEN), element.getInterestPayment());
            amount.set(element.getRemainDebt());
        });
    }
}