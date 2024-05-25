package com.argus.calculator.service.impl;

import com.argus.calculator.dto.*;
import com.argus.calculator.exception.ClientDeniedException;
import com.argus.calculator.service.CalculationService;
import com.argus.calculator.service.CreditCalculator;
import com.argus.calculator.service.RateCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.argus.calculator.model.enums.EmploymentStatus.UNEMPLOYED;
import static com.argus.calculator.util.CalculatorUtils.getAge;

@Service
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService {

    private final RateCalculator rateCalculator;

    private final CreditCalculator creditCalculator;

    @Override
    public List<LoanOfferDto> generateLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return List.of(
                generateLoanOffer(loanStatementRequestDto, false, false),
                generateLoanOffer(loanStatementRequestDto, false, true),
                generateLoanOffer(loanStatementRequestDto, true, false),
                generateLoanOffer(loanStatementRequestDto, true, true)
        );
    }

    @Override
    public CreditDto calculateCredit(ScoringDataDto scoringDataDto) {
        if (isDenied(scoringDataDto)) {
            throw new ClientDeniedException("Loan is denied");
        }
        BigDecimal amount = creditCalculator.calculateAmount(scoringDataDto.getAmount(), scoringDataDto.getIsInsuranceEnabled());
        int term = scoringDataDto.getTerm();
        BigDecimal rate = rateCalculator.calculateScoringRate(scoringDataDto);
        BigDecimal monthlyPayment = creditCalculator.calculateMonthlyPayment(amount, scoringDataDto.getTerm(), rate);
        BigDecimal psk = creditCalculator.calculatePSK(monthlyPayment, scoringDataDto.getTerm());
        List<PaymentScheduleElementDto> paymentSchedule = creditCalculator.calculatePaymentSchedule(amount, scoringDataDto.getTerm(), monthlyPayment, rate);
        return CreditDto.builder()
                .amount(amount)
                .term(term)
                .monthlyPayment(monthlyPayment.setScale(2, RoundingMode.HALF_EVEN))
                .rate(rate)
                .psk(psk)
                .isInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDto.getIsSalaryClient())
                .paymentSchedule(paymentSchedule)
                .build();
    }

    private LoanOfferDto generateLoanOffer(LoanStatementRequestDto loanStatementRequest, boolean isInsuranceEnabled, boolean isSalaryClient) {
        BigDecimal amount = loanStatementRequest.getAmount();
        BigDecimal rate = rateCalculator.calculatePrescoringRate(isInsuranceEnabled, isSalaryClient);
        BigDecimal totalAmount = creditCalculator.calculateAmount(amount, isInsuranceEnabled);
        int term = loanStatementRequest.getTerm();
        BigDecimal monthlyPayment = creditCalculator.calculateMonthlyPayment(amount, term, rate);
        return LoanOfferDto.builder()
                .requestedAmount(amount)
                .totalAmount(totalAmount)
                .term(term)
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
    }

    private Boolean isDenied(ScoringDataDto scoringDataDto) {
        if (scoringDataDto.getEmployment().getEmploymentStatus() == UNEMPLOYED) {
            return true;
        }
        BigDecimal salary = scoringDataDto.getEmployment().getSalary();
        if (scoringDataDto.getAmount().compareTo(salary.multiply(BigDecimal.valueOf(25))) > 0) {
            return true;
        }
        long age = getAge(scoringDataDto.getBirthdate());
        if (age > 60 || age < 20) {
            return true;
        }
        if (scoringDataDto.getEmployment().getWorkExperienceTotal() < 18) {
            return true;
        }
        return scoringDataDto.getEmployment().getWorkExperienceCurrent() < 3;
    }

}
