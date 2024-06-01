package com.argus.deal.service;

import com.argus.deal.entity.Client;
import com.argus.deal.entity.Statement;
import com.argus.deal.model.enums.Status;
import com.argus.deal.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatementService {

    private final StatementRepository statementRepository;

    @Transactional
    public Statement save(Client client) {
        log.info("Saving statement for client {}", client);
        Statement statement = Statement.builder()
                .client(client)
                .creationDate(LocalDateTime.now())
                .status(Status.PREAPPROVAL)
                .build();
        return statementRepository.save(statement);
    }
}
