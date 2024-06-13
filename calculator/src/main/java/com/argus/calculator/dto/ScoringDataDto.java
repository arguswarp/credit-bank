package com.argus.calculator.dto;

import com.argus.calculator.annotation.AgeLimit;
import com.argus.calculator.model.enums.MaritalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @DecimalMin(value = "30000", message = "Сумма кредита должна быть не меньше 30000")
    private BigDecimal amount;

    @NotNull(message = "Срок не должен быть пустым")
    @Min(value = 6, message = "Срок кредита должен быть не меньше 6 месяцев ")
    private Integer term;

    @Schema(type = "string", example = "John")
    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов длиной.")
    @Pattern(regexp = "^[a-zA-Z]+", message = "Имя должно состоять из латинских букв")
    private String firstName;

    @Schema(type = "string", example = "Doe")
    @NotBlank(message = "Фамилия не должна быть пустой")
    @Size(min = 2, max = 30, message = "Фамилия должна быть от 2 до 30 символов длиной.")
    @Pattern(regexp = "^[a-zA-Z]+", message = "Фамилия должна состоять из латинских букв")
    private String lastName;

    @Schema(type = "string", example = "Jack")
    @Size(min = 2, max = 30, message = "Отчество должно быть от 2 до 30 символов длиной.")
    private String middleName;

    @Schema(type = "string", example = "male")
    @NotBlank(message = "Пол не должен быть пустым")
    private String gender;

    @Schema(type = "string", example = "2024.01.01")
    @NotNull(message = "Дата рождения не должна быть пустой")
    @AgeLimit
    @JsonFormat(pattern="yyyy.MM.dd")
    private LocalDate birthdate;

    @NotBlank(message = "Серия пасспорта не должна быть пустой")
    @Size(min = 4, max = 4, message = "Серия паспорта должна состоять из 4 цифр")
    @Pattern(regexp = "^[0-9]*$", message = "Серия паспорта должна состоять только из цифр")
    private String passportSeries;

    @NotBlank(message = "Номер пасспорта не должен быть пустым")
    @Size(min = 6, max = 6, message = "Номер паспорта должен состоять из 6 цифр")
    @Pattern(regexp = "^[0-9]*$", message = "Номер паспорта должен состоять только из цифр")
    private String passportNumber;

    @Schema(type = "string", example = "2024.01.01")
    @NotNull(message = "Дата выдачи пасспорта не должна быть пустой")
    @Past(message = "Дата выдачи паспорта должна быть в прошлом")
    @JsonFormat(pattern="yyyy.MM.dd")
    private LocalDate passportIssueDate;

    @NotBlank(message = "Подразделение выдавшее паспорт не может быть пустым")
    private String passportIssueBranch;

    @NotNull(message = "Семейное положение не может быть пустым")
    private MaritalStatus maritalStatus;

    @Min(value = 0, message = "Иждивенцев не может быть меньше 0")
    private Integer dependentAmount;

    @NotNull(message = "Информация о трудоустройстве не может быть пустой")
    private EmploymentDto employment;

    @NotNull(message = "Номер аккаунта не может быть пустым")
    private String accountNumber;

    @NotNull(message = "Информация о наличии страховки должна присутствовать")
    private Boolean isInsuranceEnabled;

    @NotNull(message = "Информация о статусе зарплатного клиента должна присутствовать")
    private Boolean isSalaryClient;

}
