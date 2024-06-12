package com.argus.deal.service;

import com.argus.deal.dto.CreditDto;
import com.argus.deal.dto.LoanOfferDto;
import com.argus.deal.dto.LoanStatementRequestDto;
import com.argus.deal.dto.ScoringDataDto;
import com.argus.deal.exception.CalculatorApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * namea3cd264774bf4efb9ab609b250c5165c
 *
 * @author Maxim Chistyakov
 */
@ExtendWith(MockitoExtension.class)
class RestTemplateServiceTest {

    @InjectMocks
    private RestTemplateService restTemplateService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void getLoanOffers() {
        UUID uuid = UUID.randomUUID();

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<LoanStatementRequestDto>>any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<LoanOfferDto>>>any()))
                .thenReturn(ResponseEntity.ok(List.of(LoanOfferDto.builder().build())));

        List<LoanOfferDto> offers = restTemplateService.getLoanOffers(LoanStatementRequestDto.builder().build(), uuid);

        assertEquals(1, offers.size());
        offers.forEach(offer -> assertEquals(uuid, offer.getStatementId()));

        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<LoanStatementRequestDto>>any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<LoanOfferDto>>>any());
    }

    @Test
    void WhenHttpStatusCodeExceptionCaughtForOffers_ThenThrowCalculatorApiException() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<LoanStatementRequestDto>>any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<LoanOfferDto>>>any()))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(CalculatorApiException.class,
                () -> restTemplateService.getLoanOffers(LoanStatementRequestDto.builder().build(), UUID.randomUUID()));

        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<LoanStatementRequestDto>>any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<LoanOfferDto>>>any());
    }

    @Test
    void getCredit() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<ScoringDataDto>>any(),
                ArgumentMatchers.<ParameterizedTypeReference<CreditDto>>any()))
                .thenReturn(ResponseEntity.ok(CreditDto.builder().build()));

        CreditDto creditDto = restTemplateService.getCredit(ScoringDataDto.builder().build());

        assertNotNull(creditDto);

        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<ScoringDataDto>>any(),
                ArgumentMatchers.<ParameterizedTypeReference<CreditDto>>any());
    }

    @Test
    void WhenHttpStatusCodeExceptionCaughtForCredit_ThenThrowCalculatorApiException() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<ScoringDataDto>>any(),
                ArgumentMatchers.<ParameterizedTypeReference<CreditDto>>any()))
                .thenThrow(HttpClientErrorException.class);

        assertThrows(CalculatorApiException.class,
                () -> restTemplateService.getCredit(ScoringDataDto.builder().build()));

        verify(restTemplate).exchange(
                anyString(),
                eq(HttpMethod.POST),
                ArgumentMatchers.<HttpEntity<ScoringDataDto>>any(),
                ArgumentMatchers.<ParameterizedTypeReference<CreditDto>>any());
    }
}