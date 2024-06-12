package com.argus.deal.service;

import com.argus.deal.dto.FinishRegistrationRequestDto;
import com.argus.deal.dto.LoanStatementRequestDto;
import com.argus.deal.dto.ScoringDataDto;
import com.argus.deal.entity.Client;
import com.argus.deal.entity.Statement;
import com.argus.deal.exception.InconsistentDataException;
import com.argus.deal.model.mapper.ClientMapper;
import com.argus.deal.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ClientService.
 * <p>
 * Service to persist and get clients from db.
 * @author Maxim Chistyakov
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    public Client findOrSave(LoanStatementRequestDto loanStatementRequestDto) {
        Client client = clientMapper.loanStatementRequestDtoToClient(loanStatementRequestDto);
        Client persistentClient = findOrSaveClient(client);
        if (!checkEquality(client, persistentClient)) {
            throw new InconsistentDataException("Клиент с таким паспортом уже существует");
        }
        return persistentClient;
    }

    public ScoringDataDto prepareScoringDataDto(Statement statement, FinishRegistrationRequestDto finishRegistrationRequestDto) {
        Client client = statement.getClient();
        clientMapper.update(client, finishRegistrationRequestDto);
        Client updatedClient = clientRepository.save(client);
        return clientMapper.from(updatedClient, client.getPassport(), statement.getAppliedOffer());
    }

    private Client findOrSaveClient(Client client) {
        log.info("Searching for client {}", client);
        return clientRepository.findByPassportSeriesAndNumber(client.getPassport().getSeries(), client.getPassport().getNumber())
                .orElseGet(() -> {
                    Client newClient = clientRepository.save(client);
                    log.info("Client not found. Saving client to db: {}", newClient);
                    return newClient;
                });
    }

    private boolean checkEquality(Client fromRequest, Client fromDb) {
        return fromRequest.getFirstName().equals(fromDb.getFirstName()) &&
               fromRequest.getLastName().equals(fromDb.getLastName()) &&
               fromRequest.getEmail().equals(fromDb.getEmail()) &&
               fromRequest.getBirthdate().equals(fromDb.getBirthdate());
    }
}
