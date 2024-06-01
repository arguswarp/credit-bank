package com.argus.deal.service;

import com.argus.deal.entity.Client;
import com.argus.deal.entity.Passport;
import com.argus.deal.repository.ClientRepository;
import com.argus.deal.repository.PassportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    private final PassportRepository passportRepository;

    @Transactional
    public Client save(Client client) {
        Passport passport = client.getPassport();
        passport.setClient(client);
        log.info("Saving passport to db {}", passport);
        passportRepository.save(client.getPassport());
        log.info("Saving client to db {}", client);
        return clientRepository.save(client);
    }
}
