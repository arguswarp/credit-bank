package com.argus.calculator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class PaymentScheduleElementDto {

    private Integer number;

    @Schema(type = "string", example = "2024.01.01")
    @JsonFormat(pattern = "yyyy.MM.dd")
    private LocalDate date;

    private BigDecimal totalPayment;
    /**
     * Выплата процентов
     */
    private BigDecimal interestPayment;

    private BigDecimal debtPayment;

    private BigDecimal remainDebt;

}
