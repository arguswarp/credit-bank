package com.argus.deal.service;

import com.argus.deal.dto.*;
import com.argus.deal.entity.Client;
import com.argus.deal.entity.Credit;
import com.argus.deal.entity.Statement;
import com.argus.deal.exception.CreditAlreadyCalculatedException;
import com.argus.deal.exception.LoanAlreadyApprovedException;
import com.argus.deal.model.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealService {
    private final ClientService clientService;

    private final StatementService statementService;

    private final CreditService creditService;

    private final RestTemplateService restTemplateService;

    @Transactional
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Getting loan offers");
        Client persistentClient = clientService.findOrSave(loanStatementRequestDto);
        Statement persistentStatement = statementService.save(persistentClient);
        return restTemplateService.getLoanOffers(loanStatementRequestDto, persistentStatement.getId());
    }

    @Transactional
    public void selectOffer(LoanOfferDto loanOfferDto) {
        log.info("Selecting loan offer");
        Statement statement = statementService.get(loanOfferDto.getStatementId());
        if (statement.getStatus() != Status.PREAPPROVAL) {
            throw new LoanAlreadyApprovedException("Займ уже одобрен или отклонен");
        }
        statementService.changeStatus(statement, Status.APPROVED);
        statement.setAppliedOffer(loanOfferDto);
        statementService.update(statement);
    }

    @Transactional
    public void calculateOffer(FinishRegistrationRequestDto finishRegistrationRequestDto, UUID statementId) {
        log.info("Calculating loan offer for statement {}", statementId);
        Statement statement = statementService.get(statementId);
        if (statement.getCredit() != null) {
            throw new CreditAlreadyCalculatedException(String.format("Кредит для заявки %s уже расчитан, его id: %s", statementId, statement.getCredit().getId()));
        }
        ScoringDataDto scoringDataDto = clientService.prepareScoringDataDto(statement, finishRegistrationRequestDto);
        CreditDto creditDto = restTemplateService.getCredit(scoringDataDto);
        Credit persistentCredit = creditService.save(creditDto);
        statement.setCredit(persistentCredit);
        statementService.changeStatus(statement, Status.CC_APPROVED);
        statementService.update(statement);
    }
}
