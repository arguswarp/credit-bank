package com.argus.calculator.service.impl;

import com.argus.calculator.dto.CreditDto;
import com.argus.calculator.dto.LoanOfferDto;
import com.argus.calculator.dto.LoanStatementRequestDto;
import com.argus.calculator.dto.ScoringDataDto;
import com.argus.calculator.exception.ClientDeniedException;
import com.argus.calculator.service.CalculationService;
import com.argus.calculator.service.CreditCalculator;
import com.argus.calculator.service.RateCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        BigDecimal rate = rateCalculator.calculateScoringRate(scoringDataDto);
        BigDecimal amount = creditCalculator.calculateAmount(scoringDataDto.getAmount(), scoringDataDto.getIsInsuranceEnabled());
        BigDecimal monthlyPayment = creditCalculator.calculateMonthlyPayment(amount, scoringDataDto.getTerm(), rate);

        return CreditDto.builder()

                .build();
    }

    private LoanOfferDto generateLoanOffer(LoanStatementRequestDto loanStatementRequest, boolean isInsuranceEnabled, boolean isSalaryClient) {
        BigDecimal amount = loanStatementRequest.getAmount();
        BigDecimal rate = rateCalculator.calculatePrescoringRate(isInsuranceEnabled, isSalaryClient);
        int term = loanStatementRequest.getTerm();
        return LoanOfferDto.builder()
                .requestedAmount(amount)
                .totalAmount(creditCalculator.calculateAmount(amount, isInsuranceEnabled))
                .term(term)
                .monthlyPayment(creditCalculator.calculateMonthlyPayment(amount, term, rate))
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
