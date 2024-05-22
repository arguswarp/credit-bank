package com.argus.calculator.dto;

import com.argus.calculator.model.enums.EmploymentStatus;
import lombok.Data;

import javax.swing.text.Position;
import java.math.BigDecimal;

@Data
public class EmploymentDto {

    private EmploymentStatus employmentStatus;

    private String employerINN;

    private BigDecimal salary;

    private Position position;

    private Integer workExperienceTotal;

    private Integer workExperienceCurrent;
}
