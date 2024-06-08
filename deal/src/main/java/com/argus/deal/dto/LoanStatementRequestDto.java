package com.argus.deal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanStatementRequestDto {

    private BigDecimal amount;

    private Integer term;

    private String firstName;

    private String lastName;

    private String middleName;

    @Schema(type = "string", example = "user@mail.com")
    private String email;

    @Schema(type = "string", example = "1990.01.01")
    @JsonFormat(pattern = "yyyy.MM.dd")
    private LocalDate birthdate;

    private String passportSeries;

    private String passportNumber;
}
