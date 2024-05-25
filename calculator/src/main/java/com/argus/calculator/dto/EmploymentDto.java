package com.argus.calculator.dto;

import com.argus.calculator.model.enums.EmploymentStatus;
import com.argus.calculator.model.enums.Position;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class EmploymentDto {

    @NotNull
    private EmploymentStatus employmentStatus;

    @NotBlank
    private String employerINN;

    @Positive
    private BigDecimal salary;

    @NotNull
    private Position position;

    @Min(0)
    private Integer workExperienceTotal;

    @Min(0)
    private Integer workExperienceCurrent;
}
