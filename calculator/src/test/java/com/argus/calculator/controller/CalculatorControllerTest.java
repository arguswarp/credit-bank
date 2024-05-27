package com.argus.calculator.controller;

import com.argus.calculator.dto.CreditDto;
import com.argus.calculator.dto.LoanStatementRequestDto;
import com.argus.calculator.dto.ScoringDataDto;
import com.argus.calculator.exception.ClientDeniedException;
import com.argus.calculator.service.CalculationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculatorController.class)
@ExtendWith(SpringExtension.class)
class CalculatorControllerTest {

    private final Map <String, Object> loanOfferRequestMap = new HashMap<>();

    private final Map <String, Object> scoringDataRequest = new HashMap<>();

    private final Map <String, Object> employment = new HashMap<>();

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CalculationService calculationService;

    @BeforeEach
    void setUp() {
        loanOfferRequestMap.put("amount", "300000");
        loanOfferRequestMap.put("term", "12");
        loanOfferRequestMap.put("firstName", "John");
        loanOfferRequestMap.put("lastName", "Doe");
        loanOfferRequestMap.put("email", "john.doe@mail.com");
        loanOfferRequestMap.put("birthdate", "1990.01.01");
        loanOfferRequestMap.put("passportSeries", "1234");
        loanOfferRequestMap.put("passportNumber", "123456");

        employment.put("employmentStatus", "EMPLOYED");
        employment.put("employerINN", "1234567890");
        employment.put("salary", 100000);
        employment.put("position", "MANAGER");
        employment.put("workExperienceTotal", 360);
        employment.put("workExperienceCurrent", 36);

        scoringDataRequest.putAll(loanOfferRequestMap);
        scoringDataRequest.remove("email");
        scoringDataRequest.put("gender", "male");
        scoringDataRequest.put("passportIssueDate", "2026.09.12");
        scoringDataRequest.put("passportIssueBranch", "some branch");
        scoringDataRequest.put("maritalStatus", "MARRIED");
        scoringDataRequest.put("employment", employment);
        scoringDataRequest.put("accountNumber", "123456");
        scoringDataRequest.put("isInsuranceEnabled", false);
        scoringDataRequest.put("isSalaryClient", false);
    }
    @Test
    void sendOffers() throws Exception {
        String json = mapper.writeValueAsString(loanOfferRequestMap);

        Mockito.when(calculationService.generateLoanOffers(Mockito.any(LoanStatementRequestDto.class))).thenReturn(List.of());

        mockMvc.perform(post("/calculator/offers").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void WhenInvalidDataForOffers_ThenBadRequest() throws Exception {
        loanOfferRequestMap.replace("amount", "10000");
        loanOfferRequestMap.replace("firstName", "123");
        loanOfferRequestMap.replace("email", "@mail.com");
        String json = mapper.writeValueAsString(loanOfferRequestMap);

        mockMvc.perform(post("/calculator/offers").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void sendCredit() throws Exception {
        String json = mapper.writeValueAsString(scoringDataRequest);

        Mockito.when(calculationService.calculateCredit(Mockito.any(ScoringDataDto.class))).thenReturn(CreditDto.builder()
                .paymentSchedule(List.of())
                .build());

        mockMvc.perform(post("/calculator/calc").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void WhenInvalidDataForCredit_ThenBadRequest() throws Exception {
        scoringDataRequest.replace("amount", "0");
        String json = mapper.writeValueAsString(scoringDataRequest);

        mockMvc.perform(post("/calculator/calc").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void WhenDenialForCredit_ThenBadRequest() throws Exception {
        scoringDataRequest.replace("employmentStatus", "UNEMPLOYED");
        String json = mapper.writeValueAsString(scoringDataRequest);

        Mockito.when(calculationService.calculateCredit(Mockito.any(ScoringDataDto.class))).thenThrow(new ClientDeniedException("В займе отказано"));

        mockMvc.perform(post("/calculator/calc").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("В займе отказано"));
    }

    @Test
    void WhenAgeBelow18_ThenBadRequest() throws Exception {
        scoringDataRequest.replace("birthdate", "2020.01.01");
        String json = mapper.writeValueAsString(scoringDataRequest);

        mockMvc.perform(post("/calculator/calc").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("Возраст должен быть больше 18 лет"));
    }
}