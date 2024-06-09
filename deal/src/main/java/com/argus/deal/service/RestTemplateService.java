package com.argus.deal.service;

import com.argus.deal.dto.CreditDto;
import com.argus.deal.dto.LoanOfferDto;
import com.argus.deal.dto.LoanStatementRequestDto;
import com.argus.deal.dto.ScoringDataDto;
import com.argus.deal.exception.CalculatorApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestTemplateService {

    private final RestTemplate restTemplate;

    @Value("${deal.rest-client.root-uri}")
    private String rootUrl;

    private final String OFFERS_URL = "offers";

    private final String CREDIT_URL = "calc";

    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto, UUID statementId) {
        try {
            log.info("Executing POST: {}/{}", rootUrl, OFFERS_URL);
            ResponseEntity<List<LoanOfferDto>> response = restTemplate.exchange(OFFERS_URL,
                    HttpMethod.POST,
                    new HttpEntity<>(loanStatementRequestDto),
                    new ParameterizedTypeReference<>() {});
            List<LoanOfferDto> loanOffers = response.getBody();
            loanOffers.forEach(offer -> offer.setStatementId(statementId));
            return loanOffers;
        } catch (HttpStatusCodeException e) {
            throw new CalculatorApiException(e.getResponseBodyAsByteArray(), e.getStatusCode());
        }
    }

    public CreditDto getCredit(ScoringDataDto scoringDataDto) {
        try {
            log.info("Executing POST: {}/{}", rootUrl, CREDIT_URL);
            ResponseEntity<CreditDto> response = restTemplate.exchange(CREDIT_URL,
                    HttpMethod.POST,
                    new HttpEntity<>(scoringDataDto),
                    new ParameterizedTypeReference<>() {});
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new CalculatorApiException(e.getResponseBodyAsByteArray(), e.getStatusCode());
        }
    }

}
