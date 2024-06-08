package com.argus.deal.service;

import com.argus.deal.dto.FinishRegistrationRequestDto;
import com.argus.deal.dto.LoanOfferDto;
import com.argus.deal.entity.Credit;
import com.argus.deal.entity.Statement;
import com.argus.deal.exception.CreditAlreadyCalculatedException;
import com.argus.deal.exception.LoanAlreadyApprovedException;
import com.argus.deal.model.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {

    @InjectMocks
    private DealService dealService;

    @Mock
    private StatementService statementService;

    private Statement statement;

    @BeforeEach
    void setUp() {
        statement = Statement.builder()
                .id(UUID.randomUUID())
                .build();
    }

    @Test
    void WhenSelectOfferWithStatementStatusNotPreapproval_ThenThrowLoanAlreadyApprovedException() {
        statement.setStatus(Status.APPROVED);

        when(statementService.get(statement.getId())).thenReturn(statement);

        assertThrows(LoanAlreadyApprovedException.class,
                () -> dealService.selectOffer(LoanOfferDto.builder()
                .statementId(statement.getId())
                .build()));

        verify(statementService).get(statement.getId());
    }

    @Test
    void WhenCalculateOfferWithStatementWithCredit_ThenThrowCreditAlreadyCalculatedException() {
        statement.setCredit(Credit.builder().build());

        when(statementService.get(statement.getId())).thenReturn(statement);

        assertThrows(CreditAlreadyCalculatedException.class,
                () -> dealService.calculateOffer(FinishRegistrationRequestDto.builder().build(), statement.getId()));

        verify(statementService).get(statement.getId());
    }
}