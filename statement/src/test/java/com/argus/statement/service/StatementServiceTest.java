package com.argus.statement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void getLoanOffers() {
    }

    @Test
    void selectOffer() {
    }
}