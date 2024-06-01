package com.argus.deal.controller;

import com.argus.deal.dto.LoanOfferDto;
import com.argus.deal.dto.LoanStatementRequestDto;
import com.argus.deal.entity.Client;
import com.argus.deal.entity.Statement;
import com.argus.deal.model.mapper.ClientMapper;
import com.argus.deal.service.ClientService;
import com.argus.deal.service.RestTemplateService;
import com.argus.deal.service.StatementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("deal")
@RequiredArgsConstructor
@Slf4j
public class DealController {

    private final ClientService clientService;

    private final StatementService statementService;

    private final ClientMapper clientMapper;

    private final RestTemplateService restTemplateService;

    @PostMapping("statement")
    public List<LoanOfferDto> sendOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Received loan statement request: {}", loanStatementRequestDto);
        //TODO move to DealService
        Client transientClient = clientMapper.loanStatementRequestDtoToClient(loanStatementRequestDto);
        Client persistentClient = clientService.save(transientClient);
        Statement statement = statementService.save(persistentClient);
        return restTemplateService.getLoanOffers(loanStatementRequestDto, statement.getId());
    }

}
