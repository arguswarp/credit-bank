package com.argus.deal.controller;

import com.argus.deal.dto.ErrorResponse;
import com.argus.deal.dto.FinishRegistrationRequestDto;
import com.argus.deal.dto.LoanOfferDto;
import com.argus.deal.dto.LoanStatementRequestDto;
import com.argus.deal.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("deal")
@RequiredArgsConstructor
@Slf4j
public class DealController {

    private final DealService dealService;

    @Operation(summary = "Делает запрос к МС Калькулятор и перенаправляет его ответ с 4 кредитными предложениями с сохранением заявки и клиента в БД.")
    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос с четырьмя кредитными предложениями", responseCode = "200"),
            @ApiResponse(description = "Ошибки при валидации из МС калькулятор", responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("statement")
    @ResponseStatus(HttpStatus.OK)
    public List<LoanOfferDto> sendOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Received loan statement request: {}", loanStatementRequestDto);
        return dealService.getLoanOffers(loanStatementRequestDto);
    }

    @Operation(summary = "Принимает выбранное предложение и сохраняет его в заявку.")
    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "204"),
            @ApiResponse(description = "Ошибка если займ уже одобрен или отклонен", responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("offer/select")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void selectOffer(@RequestBody LoanOfferDto loanOfferDto) {
        log.info("Received selected loan offer: {}", loanOfferDto);
        dealService.selectOffer(loanOfferDto);
    }

    @Operation(summary = "По присланному запросу и ID заявки заполняет данные клиента, формирует и отправляет запрос к МС Кальулятор на проведение полного расчета кредита. Сохраняет полученный расчет в БД.")
    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "204"),
            @ApiResponse(description = "Ошибка если заявка не найдена или ошибки валидации из МС Калькулятор", responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("calculate/{statementId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void calculate(@Parameter(description = "ID заявки") @PathVariable(name = "statementId") UUID statementId, @RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto) {
        log.info("Received finish registration request: {}", finishRegistrationRequestDto);
        dealService.calculateOffer(finishRegistrationRequestDto, statementId);
    }

}
