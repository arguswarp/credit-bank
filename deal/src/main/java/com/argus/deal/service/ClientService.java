package com.argus.deal.service;

import com.argus.deal.dto.LoanStatementRequestDto;
import com.argus.deal.entity.Client;
import com.argus.deal.entity.Passport;
import com.argus.deal.model.mapper.ClientMapper;
import com.argus.deal.repository.ClientRepository;
import com.argus.deal.repository.PassportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    private final PassportRepository passportRepository;

    private final ClientMapper clientMapper;

    public Client save(LoanStatementRequestDto loanStatementRequestDto) {
        Client client = clientMapper.loanStatementRequestDtoToClient(loanStatementRequestDto);
        Passport passport = client.getPassport();
        passport.setClient(client);
        log.info("Saving passport to db {}", passport);
        passportRepository.save(client.getPassport());
        log.info("Saving client to db {}", client);
        return clientRepository.save(client);
    }
}
