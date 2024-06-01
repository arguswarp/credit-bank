package com.argus.deal.service;

import com.argus.deal.dto.StatementStatusHistoryDto;
import com.argus.deal.entity.Client;
import com.argus.deal.entity.Statement;
import com.argus.deal.exception.StatementNotExistException;
import com.argus.deal.model.enums.ChangeType;
import com.argus.deal.model.enums.Status;
import com.argus.deal.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatementService {

    private final StatementRepository statementRepository;

    public Statement save(Client client) {
        log.info("Saving statement for client {}", client);
        Status status = Status.PREAPPROVAL;
        LocalDateTime time = LocalDateTime.now();
        Statement statement = Statement.builder()
                .client(client)
                .creationDate(time)
                .status(status)
                .statusHistory(List.of(StatementStatusHistoryDto.builder()
                        .status(status)
                        .time(time)
                        .changeType(ChangeType.AUTOMATIC)
                        .build()))
                .build();
        return statementRepository.save(statement);
    }

    public Statement update(Statement statement) {
        log.info("Updating statement {}", statement);
        return statementRepository.save(statement);
    }

    public Statement get(UUID statementId) {
        return statementRepository.findById(statementId)
                .orElseThrow(()-> new StatementNotExistException("Заявка с таким UUID не существует: " + statementId));
    }
    //TODO mb null status history, check
    public void changeStatus(Statement statement, Status status) {
        statement.setStatus(status);
        statement.getStatusHistory().add(StatementStatusHistoryDto.builder()
                .status(status)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build());
    }
}
