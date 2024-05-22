package com.argus.calculator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanStatementRequestDto {
    @NotNull
    @DecimalMin("30000")
    private BigDecimal amount;
    /**
     * Срок кредита
     */
    @NotNull
    @Min(6)
    private Integer term;

    @NotBlank
    @Size(min = 2, max = 30)
    //TODO mb modify pattern
    @Pattern(regexp = "^[a-zA-Z]+")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^+[a-zA-Z]+")
    private String lastName;

    private String middleName;
    //TODO check if it fits the task
    @NotBlank
    @Email
    private String email;
    //TODO custom annotation to check if older 18
    @NotNull
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    @PastOrPresent
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy.MM.dd")
    private LocalDate birthdate;

    @NotBlank
    @Size(min = 4, max = 4)
    private String passportSeries;

    @NotBlank
    @Size(min = 6, max = 6)
    private String passportNumber;
}
