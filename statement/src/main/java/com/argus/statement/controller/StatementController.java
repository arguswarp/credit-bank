package com.argus.statement.controller;

import com.argus.statement.dto.ErrorResponse;
import com.argus.statement.dto.LoanOfferDto;
import com.argus.statement.dto.LoanStatementRequestDto;
import com.argus.statement.service.StatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * StatementController
 *
 * @author Maxim Chistyakov
 */
@RestController
@RequestMapping("statement")
@RequiredArgsConstructor
public class StatementController {

    private final StatementService statementService;

    @Operation(summary = "Выполняет прескоринг и делает запрос к МС Сделка. Перенаправляет его ответ с 4 кредитными предложениями.")
    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос с четырьмя кредитными предложениями", responseCode = "200"),
            @ApiResponse(description = "Ошибки валидации или ошибка API МС Сделка", responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LoanOfferDto> sendOffers(@Valid @RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        return statementService.getLoanOffers(loanStatementRequestDto);
    }

    @Operation(summary = "Принимает выбранное предложение и отправляет его в МС Сделка для сохранения в БД")
    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос.", responseCode = "204"),
            @ApiResponse(description = "Ошибка API МС Сделка", responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("offer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void selectOffer(@RequestBody LoanOfferDto loanOfferDto) {
        statementService.selectOffer(loanOfferDto);
    }
}
