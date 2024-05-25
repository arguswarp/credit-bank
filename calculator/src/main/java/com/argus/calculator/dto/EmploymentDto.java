package com.argus.calculator.dto;

import com.argus.calculator.model.enums.EmploymentStatus;
import com.argus.calculator.model.enums.Position;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDto {

    @NotNull(message = "Трудовой статус не может быть пустым")
    private EmploymentStatus employmentStatus;

    @NotBlank(message = "ИНН работодателя не может быть пустым")
    private String employerINN;

    @NotNull(message = "Зарплата не может быть пустой")
    @Positive(message = "Зарплата должна быть больше 0")
    private BigDecimal salary;

    @NotNull(message = "Должность не может быть пустой")
    private Position position;

    @NotNull(message = "Общий трудовой стаж не может быть пустым")
    @Min(0)
    private Integer workExperienceTotal;

    @NotNull(message = "Трудовой стаж на текущем месте работы не может быть пустым")
    @Min(0)
    private Integer workExperienceCurrent;
}
