package com.argus.deal.entity;

import com.argus.deal.model.enums.EmploymentStatus;
import com.argus.deal.model.enums.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Employment.
 *
 * @author Maxim Chistyakov
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Employment {

    private UUID id;

    private EmploymentStatus employmentStatus;

    private String employerINN;

    private BigDecimal salary;

    private Position position;

    private Integer workExperienceTotal;

    private Integer workExperienceCurrent;

}
