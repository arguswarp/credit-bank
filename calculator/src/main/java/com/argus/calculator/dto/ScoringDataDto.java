package com.argus.calculator.dto;

import com.argus.calculator.model.enums.MaritalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoringDataDto {

    @NotNull(message = "Количество не должно быть пустым")
    @DecimalMin(value = "20000", message = "Сумма кредита должна быть не меньше 30000")
    private BigDecimal amount;

    @NotNull(message = "Срок не должен быть пустым")
    @Min(value = 6, message = "Срок кредита должен быть не меньше 6 месяцев ")
    private Integer term;

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов длиной.")
    @Pattern(regexp = "^[a-zA-Z]+", message = "Имя должно состоять из латинских букв")
    private String firstName;

    @NotBlank(message = "Фамилия не должна быть пустой")
    @Size(min = 2, max = 30, message = "Фамилия должна быть от 2 до 30 символов длиной.")
    @Pattern(regexp = "^[a-zA-Z]+", message = "Фамилия должна состоять из латинских букв")
    private String lastName;

    private String middleName;

    @NotBlank(message = "Пол не должен быть пустым")
    private String gender;

    //TODO custom annotation to check if older 18
    @NotNull
    @Past
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy.MM.dd")
    private LocalDate birthdate;

    @NotBlank(message = "Серия пасспорта не должна быть пустой")
    @Size(min = 4, max = 4, message = "Серия паспорта должна состоять из 4 цифр")
    @Pattern(regexp = "^[0-9]*$", message = "Серия паспорта должна состоять только из цифр")
    private String passportSeries;

    @NotBlank(message = "Номер пасспорта не должен быть пустым")
    @Size(min = 6, max = 6, message = "Номер паспорта должен состоять из 6 цифр")
    @Pattern(regexp = "^[0-9]*$", message = "Номер паспорта должен состоять только из цифр")
    private String passportNumber;

    @NotNull
    @Future
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy.MM.dd")
    private LocalDate passportIssueDate;

    @NotBlank
    private String passportIssueBranch;

    @NotNull
    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    @NotNull
    private EmploymentDto employment;

    @NotNull
    private String accountNumber;

    private Boolean isInsuranceEnabled;

    private Boolean isSalaryClient;

}
