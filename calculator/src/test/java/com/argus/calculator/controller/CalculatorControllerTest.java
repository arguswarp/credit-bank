package com.argus.calculator.controller;

import com.argus.calculator.dto.CreditDto;
import com.argus.calculator.dto.LoanStatementRequestDto;
import com.argus.calculator.dto.ScoringDataDto;
import com.argus.calculator.exception.ClientDeniedException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    void WhenInvalidDataForOffers_ThenBadRequest() throws Exception {
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
    void sendCredit() throws Exception {
        String requestBody = """
                {
                  "amount": 20000,
                  "term": 36,
                  "firstName": "John",
                  "lastName": "Doe",
                  "middleName": "",
                  "gender": "male",
                  "birthdate": "1990.01.01",
                  "passportSeries": "1234",
                  "passportNumber": "123456",
                  "passportIssueDate": "2026.09.12",
                  "passportIssueBranch": "some branch",
                  "maritalStatus": "MARRIED",
                  "dependentAmount": 0,
                  "employment": {
                    "employmentStatus": "EMPLOYED",
                    "employerINN": "1234",
                    "salary": 100000,
                    "position": "MANAGER",
                    "workExperienceTotal": 360,
                    "workExperienceCurrent": 36
                  },
                  "accountNumber": "123456",
                  "isInsuranceEnabled": false,
                  "isSalaryClient": false
                }""";

        Mockito.when(calculationService.calculateCredit(Mockito.any(ScoringDataDto.class))).thenReturn(CreditDto.builder()
                .paymentSchedule(List.of())
                .build());

        mockMvc.perform(post("/calculator/calc").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void WhenInvalidDataForCredit_ThenBadRequest() throws Exception {
        String requestBody = """
                {
                  "amount": 0,
                  "term": 36,
                  "firstName": "John",
                  "lastName": "Doe",
                  "middleName": "",
                  "gender": "male",
                  "birthdate": "1990.01.01",
                  "passportSeries": "1234",
                  "passportNumber": "123456",
                  "passportIssueDate": "2026.09.12",
                  "passportIssueBranch": "some branch",
                  "maritalStatus": "MARRIED",
                  "dependentAmount": 0,
                  "employment": {
                    "employmentStatus": "EMPLOYED",
                    "employerINN": "1234",
                    "salary": 100000,
                    "position": "MANAGER",
                    "workExperienceTotal": 360,
                    "workExperienceCurrent": 36
                  },
                  "accountNumber": "123456",
                  "isInsuranceEnabled": false,
                  "isSalaryClient": false
                }""";

        Mockito.when(calculationService.calculateCredit(Mockito.any(ScoringDataDto.class))).thenReturn(CreditDto.builder()
                .paymentSchedule(List.of())
                .build());

        mockMvc.perform(post("/calculator/calc").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void WhenDenialForCredit_ThenBadRequest() throws Exception {
        String requestBody = """
                {
                  "amount": 30000,
                  "term": 36,
                  "firstName": "John",
                  "lastName": "Doe",
                  "middleName": "",
                  "gender": "male",
                  "birthdate": "1990.01.01",
                  "passportSeries": "1234",
                  "passportNumber": "123456",
                  "passportIssueDate": "2026.09.12",
                  "passportIssueBranch": "some branch",
                  "maritalStatus": "MARRIED",
                  "dependentAmount": 0,
                  "employment": {
                    "employmentStatus": "UNEMPLOYED",
                    "employerINN": "1234",
                    "salary": 1,
                    "position": "MANAGER",
                    "workExperienceTotal": 360,
                    "workExperienceCurrent": 36
                  },
                  "accountNumber": "123456",
                  "isInsuranceEnabled": false,
                  "isSalaryClient": false
                }""";

        Mockito.when(calculationService.calculateCredit(Mockito.any(ScoringDataDto.class))).thenThrow(new ClientDeniedException("В займе отказано"));

        mockMvc.perform(post("/calculator/calc").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("В займе отказано"));
    }
}