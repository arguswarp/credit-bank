package com.argus.deal.dto;

import com.argus.deal.model.enums.MaritalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ScoringDataDto.
 *
 * @author Maxim Chistyakov
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoringDataDto {

    private BigDecimal amount;

    private Integer term;

    private String firstName;

    private String lastName;

    private String middleName;

    private String gender;

    @JsonFormat(pattern = "yyyy.MM.dd")
    private LocalDate birthdate;

    private String passportSeries;

    private String passportNumber;

    @JsonFormat(pattern = "yyyy.MM.dd")
    private LocalDate passportIssueDate;

    private String passportIssueBranch;

    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    private EmploymentDto employment;

    private String accountNumber;

    private Boolean isInsuranceEnabled;

    private Boolean isSalaryClient;

}
