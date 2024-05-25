package com.argus.calculator.controller;

import com.argus.calculator.dto.LoanStatementRequestDto;
import com.argus.calculator.service.CalculationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculatorController.class)
@ExtendWith(SpringExtension.class)
class CalculatorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CalculationService calculationService;

    @Test
    void sendOffers() throws Exception {
        String requestBody = """
                {
                  "amount": 300000,
                  "term": 12,
                  "firstName": "John",
                  "lastName": "Doe",
                  "middleName": "",
                  "email": "john.doe@mail.com",
                  "birthdate": "1990.01.01",
                  "passportSeries": "1234",
                  "passportNumber": "123456"
                }""";

        Mockito.when(calculationService.generateLoanOffers(Mockito.any(LoanStatementRequestDto.class))).thenReturn(List.of());

        mockMvc.perform(post("/calculator/offers").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void WhenInvalidData_ThenBadRequest() throws Exception {
        String requestBody = """
                {
                  "amount": 10000,
                  "term": 12,
                  "firstName": "123",
                  "lastName": "Doe",
                  "middleName": "",
                  "email": "@mail.com",
                  "birthdate": "1990.01.01",
                  "passportSeries": "1234",
                  "passportNumber": "123456"
                }""";

        Mockito.when(calculationService.generateLoanOffers(Mockito.any(LoanStatementRequestDto.class))).thenReturn(List.of());

        mockMvc.perform(post("/calculator/offers").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }
    @Test
    void sendCredit() {
    }
}