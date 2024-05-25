package com.argus.calculator.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class PaymentScheduleElementDto {

    private Integer number;

    private LocalDate date;

    private BigDecimal totalPayment;
    /**
     * Выплата процентов
     */
    private BigDecimal interestPayment;

    private BigDecimal debtPayment;

    private BigDecimal remainDebt;

}
