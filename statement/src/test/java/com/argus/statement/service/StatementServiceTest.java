package com.argus.statement.service;

import com.argus.statement.dto.LoanOfferDto;
import com.argus.statement.dto.LoanStatementRequestDto;
import com.argus.statement.exception.DealApiException;
import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * StatementServiceTest
 *
 * @author Maxim Chistyakov
 */
@ExtendWith(MockitoExtension.class)
class StatementServiceTest {

    @InjectMocks
    private StatementService statementService;

    @Mock
    private DealFeignClient dealFeignClient;

    private static final FeignException FEIGN_EXCEPTION = FeignException.errorStatus("Get loan offers",
            Response.builder()
                    .status(404)
                    .headers(new HashMap<>())
                    .reason("Error in Deal")
                    .request(Request.create(
                            Request.HttpMethod.POST,
                            "http://localhost:8081",
                            Map.of(),
                            null,
                            null,
                            null
                    ))
                    .build());

    @Test
    void getLoanOffers() {
        when(dealFeignClient.getLoanOffers(any(LoanStatementRequestDto.class))).thenReturn(List.of(LoanOfferDto.builder()
                        .requestedAmount(BigDecimal.TEN)
                        .term(36)
                        .rate(BigDecimal.TEN)
                .build()));

        List<LoanOfferDto> offers = statementService.getLoanOffers(LoanStatementRequestDto.builder().build());

        assertNotNull(offers);
        assertEquals(1, offers.size());
        assertEquals(BigDecimal.TEN, offers.get(0).getRequestedAmount());
        assertEquals(BigDecimal.TEN, offers.get(0).getRate());
        assertEquals(36, offers.get(0).getTerm());

        verify(dealFeignClient).getLoanOffers(any(LoanStatementRequestDto.class));
    }

    @Test
    void whenFeignExceptionOccuredWhileGetOffers_ThenThrowDealApiException() {
        when(dealFeignClient.getLoanOffers(any(LoanStatementRequestDto.class)))
                .thenThrow(FEIGN_EXCEPTION);

        assertThrows(DealApiException.class, () -> statementService.getLoanOffers(LoanStatementRequestDto.builder().build()));

        verify(dealFeignClient).getLoanOffers(any(LoanStatementRequestDto.class));
    }

    @Test
    void whenEmptyBodyInErrorResponseWhileGetOffers_ThenThrowRuntimeException() {
        when(dealFeignClient.getLoanOffers(any(LoanStatementRequestDto.class)))
                .thenThrow(FeignException.class);

        assertThrows(RuntimeException.class, () -> statementService.getLoanOffers(LoanStatementRequestDto.builder().build()));

        verify(dealFeignClient).getLoanOffers(any(LoanStatementRequestDto.class));
    }

    @Test
    void selectOffer() {
        dealFeignClient.selectOffer(LoanOfferDto.builder().build());

        verify(dealFeignClient).selectOffer(any(LoanOfferDto.class));
    }

    @Test
    void dealApiExceptionOccuredWhileSelectOffers_ThenThrowDealApiException() {
        doThrow(FEIGN_EXCEPTION).when(dealFeignClient).selectOffer(any(LoanOfferDto.class));

        assertThrows(DealApiException.class, () -> statementService.selectOffer(LoanOfferDto.builder().build()));

        verify(dealFeignClient).selectOffer(any(LoanOfferDto.class));
    }

    @Test
    void whenEmptyBodyInErrorResponseWhileSelectOffer_ThenThrowRuntimeException() {
        doThrow(FeignException.class).when(dealFeignClient).selectOffer(any(LoanOfferDto.class));

        assertThrows(RuntimeException.class, () -> statementService.selectOffer(LoanOfferDto.builder().build()));

        verify(dealFeignClient).selectOffer(any(LoanOfferDto.class));
    }
}