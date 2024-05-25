package com.argus.calculator.service;

import com.argus.calculator.dto.ScoringDataDto;
import com.argus.calculator.model.enums.EmploymentStatus;
import com.argus.calculator.model.enums.MaritalStatus;
import com.argus.calculator.model.enums.Position;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.argus.calculator.model.enums.EmploymentStatus.BUSINESS_OWNER;
import static com.argus.calculator.model.enums.EmploymentStatus.SELF_EMPLOYED;
import static com.argus.calculator.model.enums.MaritalStatus.DIVORCED;
import static com.argus.calculator.model.enums.MaritalStatus.MARRIED;
import static com.argus.calculator.model.enums.Position.MANAGER;
import static com.argus.calculator.model.enums.Position.TOP_MANAGER;
import static com.argus.calculator.util.CalculatorUtils.getAge;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
//TODO magic numbers to props
@AllArgsConstructor
@Component
@Slf4j
public class RateCalculator {

    @Value("${calculator.base-rate:15}")
    private BigDecimal BASE_RATE;

    @Value("${calculator.insurance.rate-reduction}")
    private BigDecimal INSURANCE_RATE_REDUCTION;

    @Value("${calculator.client.rate-reduction}")
    private BigDecimal CLIENT_RATE_REDUCTION;


    public BigDecimal calculatePrescoringRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        log.info("Calculating prescoring rate");
        return BASE_RATE.subtract(isInsuranceEnabled ? INSURANCE_RATE_REDUCTION : ZERO)
                .subtract(isSalaryClient ? CLIENT_RATE_REDUCTION : ZERO);
    }

    public BigDecimal calculateScoringRate(ScoringDataDto scoringDataDto) {
        log.info("Calculating scoring rate");
        BigDecimal rate = calculatePrescoringRate(scoringDataDto.getIsInsuranceEnabled(), scoringDataDto.getIsSalaryClient());
        return rate.add(rateAdditionByEmployment(scoringDataDto.getEmployment().getEmploymentStatus()))
                .add(rateAdditionByPosition(scoringDataDto.getEmployment().getPosition()))
                .add(rateAdditionByMaritalStatus(scoringDataDto.getMaritalStatus()))
                .add(rateAdditionByGenderAndAge(scoringDataDto.getGender(), getAge(scoringDataDto.getBirthdate())));
    }

    private BigDecimal rateAdditionByEmployment(EmploymentStatus employmentStatus) {
        if (employmentStatus == SELF_EMPLOYED) {
            return ONE;
        }
        if (employmentStatus == BUSINESS_OWNER) {
            return BigDecimal.valueOf(3);
        }
        return ZERO;
    }

    private BigDecimal rateAdditionByPosition(Position position) {
        if (position == MANAGER) {
            return BigDecimal.valueOf(2).negate();
        }
        if (position == TOP_MANAGER) {
            return BigDecimal.valueOf(3).negate();
        }
        return ZERO;
    }

    private BigDecimal rateAdditionByMaritalStatus(MaritalStatus maritalStatus) {
        if (maritalStatus == MARRIED) {
            return BigDecimal.valueOf(3).negate();
        }
        if (maritalStatus == DIVORCED) {
            return ONE;
        }
        return ZERO;
    }

    private BigDecimal rateAdditionByGenderAndAge(String gender, long age) {
        if (gender.equals("female") && (age >= 32 && age <= 60)) {
            return BigDecimal.valueOf(3).negate();
        } else if (gender.equals("male") && (age >= 30 && age <= 50)) {
            return BigDecimal.valueOf(3).negate();
        } else {
            return BigDecimal.valueOf(7);
        }
    }
}
