package com.argus.deal.service;

import com.argus.deal.dto.StatementStatusHistoryDto;
import com.argus.deal.entity.Client;
import com.argus.deal.entity.Statement;
import com.argus.deal.exception.StatementNotExistException;
import com.argus.deal.model.enums.ChangeType;
import com.argus.deal.model.enums.Status;
import com.argus.deal.repository.StatementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * StatementServiceTest.
 *
 * @author Maxim Chistyakov
 */
@ExtendWith(MockitoExtension.class)
class StatementServiceTest {

    @InjectMocks
    private StatementService statementService;

    @Mock
    private StatementRepository statementRepository;

    private Client client;

    private Statement statement;

    @BeforeEach
    void setUp() {
        List<StatementStatusHistoryDto> statuses = new ArrayList<>();
        statuses.add(StatementStatusHistoryDto.builder()
                .status(Status.PREAPPROVAL)
                .changeType(ChangeType.AUTOMATIC)
                .build());

        statement = Statement.builder()
                .id(UUID.randomUUID())
                .client(client)
                .status(Status.PREAPPROVAL)
                .statusHistory(statuses)
                .build();
    }

    @Test
    void save() {
        client = Client.builder()
                .id(UUID.randomUUID())
                .build();

        doAnswer(returnsFirstArg()).when(statementRepository).save(any(Statement.class));

        Statement savedStatement = statementService.save(client);

        assertNotNull(savedStatement);
        assertEquals(client, savedStatement.getClient());
        assertNotNull(savedStatement.getCreationDate());
        assertEquals(1, savedStatement.getStatusHistory().size());
        assertNotNull(savedStatement.getStatusHistory().get(0).getTime());
        assertEquals(statement.getStatus(), savedStatement.getStatus());
        assertEquals(statement.getStatusHistory().get(0).getStatus(), savedStatement.getStatusHistory().get(0).getStatus());
        assertEquals(statement.getStatusHistory().get(0).getChangeType(), savedStatement.getStatusHistory().get(0).getChangeType());

        verify(statementRepository).save(any(Statement.class));
    }

    @Test
    void update() {
        doAnswer(returnsFirstArg()).when(statementRepository).save(any(Statement.class));

        Statement updatedStatement = statementService.update(statement);

        assertNotNull(updatedStatement);
        assertEquals(statement, updatedStatement);

        verify(statementRepository).save(any(Statement.class));
    }

    @Test
    void get() {
        when(statementRepository.findById(statement.getId())).thenReturn(Optional.ofNullable(statement));

        Statement expectedStatement = statementService.get(statement.getId());

        assertNotNull(expectedStatement);
        assertEquals(statement, expectedStatement);

        verify(statementRepository).findById(statement.getId());
    }

    @Test
    void WhenStatementNotFound_ThenThrowStatementNotExistException() {
        when(statementRepository.findById(statement.getId())).thenReturn(Optional.empty());

        assertThrows(StatementNotExistException.class, () -> statementService.get(statement.getId()));

        verify(statementRepository).findById(statement.getId());
    }

    @Test
    void changeStatus() {
        statementService.changeStatus(statement, Status.APPROVED);

        assertEquals(Status.APPROVED, statement.getStatus());
        assertEquals(2, statement.getStatusHistory().size());
    }
}