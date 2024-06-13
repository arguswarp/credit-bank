package com.argus.deal.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * LoanOfferDto.
 *
 * @author Maxim Chistyakov
 */
@Builder
@Data
public class LoanOfferDto {

    private UUID statementId;

    private BigDecimal requestedAmount;

    private BigDecimal totalAmount;

    private Integer term;

    private BigDecimal monthlyPayment;

    private BigDecimal rate;

    private Boolean isInsuranceEnabled;

    private Boolean isSalaryClient;
}
