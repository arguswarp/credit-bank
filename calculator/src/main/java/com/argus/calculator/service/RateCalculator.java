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

    @Value("${calculator.employment.self-employed-rate-addition}")
    private BigDecimal SELF_EMPLOYED_RATE_ADDITION;

    @Value("${calculator.employment.business-owner-rate-addition}")
    private BigDecimal BUSINESS_OWNER_RATE_ADDITION;

    @Value("${calculator.position.manager-rate-reduction}")
    private BigDecimal MANAGER_RATE_REDUCTION;

    @Value("${calculator.position.top-manager-rate-reduction}")
    private BigDecimal TOP_MANAGER_RATE_REDUCTION;

    @Value("${calculator.marital-status.married-rate-reduction}")
    private BigDecimal MARRIED_RATE_REDUCTION;

    @Value("${calculator.marital-status.divorced-rate-addition}")
    private BigDecimal DIVORCED_RATE_ADDITION;

    @Value("${calculator.gender.male-rate-reduction}")
    private BigDecimal MALE_RATE_REDUCTION;

    @Value("${calculator.gender.female-rate-reduction}")
    private BigDecimal FEMALE_RATE_REDUCTION;

    @Value("${calculator.gender.non-binary-rate-addition}")
    private BigDecimal NON_BINARY_RATE_ADDITION;

    private final String MALE = "male";

    private final String FEMALE = "female";



    public BigDecimal calculatePrescoringRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        log.info("Calculating prescoring rate");
        return BASE_RATE.add(isInsuranceEnabled ? INSURANCE_RATE_REDUCTION : ZERO)
                .add(isSalaryClient ? CLIENT_RATE_REDUCTION : ZERO);
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
            return SELF_EMPLOYED_RATE_ADDITION;
        }
        if (employmentStatus == BUSINESS_OWNER) {
            return BUSINESS_OWNER_RATE_ADDITION;
        }
        return ZERO;
    }

    private BigDecimal rateAdditionByPosition(Position position) {
        if (position == MANAGER) {
            return MANAGER_RATE_REDUCTION;
        }
        if (position == TOP_MANAGER) {
            return TOP_MANAGER_RATE_REDUCTION;
        }
        return ZERO;
    }

    private BigDecimal rateAdditionByMaritalStatus(MaritalStatus maritalStatus) {
        if (maritalStatus == MARRIED) {
            return MARRIED_RATE_REDUCTION;
        }
        if (maritalStatus == DIVORCED) {
            return DIVORCED_RATE_ADDITION;
        }
        return ZERO;
    }

    private BigDecimal rateAdditionByGenderAndAge(String gender, long age) {
        if (gender.equalsIgnoreCase(FEMALE) && (age >= 32 && age <= 60)) {
            return FEMALE_RATE_REDUCTION;
        } else if (gender.equalsIgnoreCase(MALE) && (age >= 30 && age <= 50)) {
            return MALE_RATE_REDUCTION;
        } else if (age >= 18){
            return NON_BINARY_RATE_ADDITION;
        }else {
            return ZERO;
        }
    }
}
