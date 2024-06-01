package com.argus.deal.service;

import com.argus.deal.dto.LoanOfferDto;
import com.argus.deal.dto.LoanStatementRequestDto;
import com.argus.deal.entity.Client;
import com.argus.deal.entity.Statement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealService {
    private final ClientService clientService;

    private final StatementService statementService;

    private final RestTemplateService restTemplateService;

    @Transactional
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Getting loan offers");
        Client persistentClient = clientService.save(loanStatementRequestDto);
        Statement persistentStatement = statementService.save(persistentClient);
        return restTemplateService.getLoanOffers(loanStatementRequestDto, persistentStatement.getId());
    }
}
