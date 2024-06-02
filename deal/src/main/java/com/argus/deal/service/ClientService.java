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

    public Client findOrSave(LoanStatementRequestDto loanStatementRequestDto) {
        Client client = clientMapper.loanStatementRequestDtoToClient(loanStatementRequestDto);
        Client persistentClient = findOrSaveClient(client);
        Passport passport = client.getPassport();
        passport.setClient(client);
        findOrSavePassport(passport);
        return persistentClient;
    }

    public Client findOrSaveClient(Client client) {
        log.info("Searching for client {}", client);
        return clientRepository.findByPassportSeriesAndPassportNumber(client.getPassport().getSeries(), client.getPassport().getNumber())
                .orElseGet(() -> {
                    log.info("Saving client to db {}", client);
                    return clientRepository.save(client);
                });
    }

    public Passport findOrSavePassport(Passport passport) {
        log.info("Searching for passport {} {}", passport.getSeries(), passport.getNumber());
        return passportRepository.findBySeriesAndNumber(passport.getSeries(), passport.getNumber())
                .orElseGet(() -> {
                    log.info("Saving passport to db {}", passport);
                    return passportRepository.save(passport);
                });
    }
}
