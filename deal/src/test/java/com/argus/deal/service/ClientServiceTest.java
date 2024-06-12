package com.argus.deal.service;

import com.argus.deal.dto.*;
import com.argus.deal.entity.Client;
import com.argus.deal.entity.Employment;
import com.argus.deal.entity.Passport;
import com.argus.deal.entity.Statement;
import com.argus.deal.exception.InconsistentDataException;
import com.argus.deal.model.enums.EmploymentStatus;
import com.argus.deal.model.enums.Gender;
import com.argus.deal.model.enums.MaritalStatus;
import com.argus.deal.model.enums.Position;
import com.argus.deal.model.mapper.ClientMapper;
import com.argus.deal.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ${NAME}.
 *
 * @author Maxim Chistyakov
 */
@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    private LoanStatementRequestDto loanStatementRequestDto;

    private Client client;

    private Passport passport;

    @BeforeEach
    void setUp() {
        loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(100_000))
                .term(36)
                .firstName("John")
                .lastName("Doe")
                .email("john@mail.com")
                .birthdate(LocalDate.of(1990, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        passport = Passport.builder()
                .series(loanStatementRequestDto.getPassportSeries())
                .number(loanStatementRequestDto.getPassportNumber())
                .build();

        client = Client.builder()
                .id(UUID.randomUUID())
                .firstName(loanStatementRequestDto.getFirstName())
                .lastName(loanStatementRequestDto.getLastName())
                .email(loanStatementRequestDto.getEmail())
                .birthdate(loanStatementRequestDto.getBirthdate())
                .passport(passport)
                .build();
    }

    @Test
    void findOrSave() {
        when(clientMapper.loanStatementRequestDtoToClient(loanStatementRequestDto)).thenReturn(client);
        when(clientRepository.findByPassportSeriesAndNumber(client.getPassport().getSeries(), client.getPassport().getNumber()))
                .thenReturn(Optional.empty());
        when(clientRepository.save(client)).thenReturn(client);

        Client clientSaved = clientService.findOrSave(loanStatementRequestDto);
        assertClientEquals(client, clientSaved);

        verify(clientMapper).loanStatementRequestDtoToClient(loanStatementRequestDto);
        verify(clientRepository).findByPassportSeriesAndNumber(client.getPassport().getSeries(), client.getPassport().getNumber());
        verify(clientRepository).save(client);
    }

    @Test
    void WhenClientFound_ThenThisClientReturned() {
        when(clientMapper.loanStatementRequestDtoToClient(loanStatementRequestDto)).thenReturn(client);
        when(clientRepository.findByPassportSeriesAndNumber(client.getPassport().getSeries(), client.getPassport().getNumber()))
                .thenReturn(Optional.of(client));

        Client clientSaved = clientService.findOrSave(loanStatementRequestDto);
        assertClientEquals(client, clientSaved);

        verify(clientMapper).loanStatementRequestDtoToClient(loanStatementRequestDto);
        verify(clientRepository).findByPassportSeriesAndNumber(client.getPassport().getSeries(), client.getPassport().getNumber());
        verify(clientRepository, never()).save(client);
    }

    @Test
    void WhenClientHasTheSamePassport_ThenThrowInconsistentDataException() {
        when(clientMapper.loanStatementRequestDtoToClient(loanStatementRequestDto)).thenReturn(client);
        when(clientRepository.findByPassportSeriesAndNumber(client.getPassport().getSeries(), client.getPassport().getNumber()))
                .thenReturn(Optional.of(Client.builder()
                        .firstName("John")
                        .lastName("but not Doe")
                        .passport(passport)
                        .build()));

        assertThrows(InconsistentDataException.class, () -> clientService.findOrSave(loanStatementRequestDto));

        verify(clientMapper).loanStatementRequestDtoToClient(loanStatementRequestDto);
        verify(clientRepository).findByPassportSeriesAndNumber(client.getPassport().getSeries(), client.getPassport().getNumber());
        verify(clientRepository, never()).save(client);
    }

    @Test
    void prepareScoringDataDto() {
        FinishRegistrationRequestDto finishRegistrationRequestDto = FinishRegistrationRequestDto.builder()
                .gender(Gender.MALE)
                .passportIssueBranch("some branch")
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .workExperienceCurrent(36)
                        .workExperienceTotal(360)
                        .position(Position.MANAGER)
                        .salary(BigDecimal.valueOf(100_000))
                        .employerINN("1234567890")
                        .build())
                .accountNumber("123456")
                .passportIssueDate(LocalDate.of(2000, 9, 12))
                .dependentAmount(1)
                .maritalStatus(MaritalStatus.MARRIED)
                .build();

        Statement statement = Statement.builder()
                .client(client)
                .appliedOffer(LoanOfferDto.builder()
                        .requestedAmount(loanStatementRequestDto.getAmount())
                        .term(loanStatementRequestDto.getTerm())
                        .rate(BigDecimal.valueOf(10))
                        .monthlyPayment(BigDecimal.valueOf(1000))
                        .isInsuranceEnabled(true)
                        .build())
                .build();

        Answer<Void> answer = invocation -> {
            Object args0 = invocation.getArgument(0);
            Object args1 = invocation.getArgument(1);
            client = (Client) args0;
            FinishRegistrationRequestDto finishRegistrationRequestDto1 = (FinishRegistrationRequestDto) args1;

            client.setGender(finishRegistrationRequestDto1.getGender());
            client.getPassport().setIssueBranch(finishRegistrationRequestDto1.getPassportIssueBranch());
            client.getPassport().setIssueDate(finishRegistrationRequestDto1.getPassportIssueDate());
            client.setEmployment(Employment.builder()
                    .workExperienceCurrent(finishRegistrationRequestDto1.getEmployment().getWorkExperienceCurrent())
                    .workExperienceTotal(finishRegistrationRequestDto1.getEmployment().getWorkExperienceTotal())
                    .position(finishRegistrationRequestDto1.getEmployment().getPosition())
                    .salary(finishRegistrationRequestDto1.getEmployment().getSalary())
                    .employerINN(finishRegistrationRequestDto1.getEmployment().getEmployerINN())
                    .employmentStatus(finishRegistrationRequestDto1.getEmployment().getEmploymentStatus())
                    .build());
            client.setAccountNumber(finishRegistrationRequestDto1.getAccountNumber());
            client.setDependentAmount(finishRegistrationRequestDto1.getDependentAmount());
            client.setMaritalStatus(finishRegistrationRequestDto1.getMaritalStatus());
            return null;
        };

        doAnswer(answer).when(clientMapper).update(client, finishRegistrationRequestDto);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.from(client, client.getPassport(), statement.getAppliedOffer()))
                .thenReturn(ScoringDataDto.builder()
                        .amount(statement.getAppliedOffer().getRequestedAmount())
                        .term(statement.getAppliedOffer().getTerm())
                        .firstName(client.getFirstName())
                        .lastName(client.getLastName())
                        .middleName(client.getMiddleName())
                        .gender(finishRegistrationRequestDto.getGender().toString())
                        .birthdate(client.getBirthdate())
                        .employment(finishRegistrationRequestDto.getEmployment())
                        .accountNumber(finishRegistrationRequestDto.getAccountNumber())
                        .passportIssueBranch(finishRegistrationRequestDto.getPassportIssueBranch())
                        .passportIssueDate(finishRegistrationRequestDto.getPassportIssueDate())
                        .dependentAmount(finishRegistrationRequestDto.getDependentAmount())
                        .maritalStatus(finishRegistrationRequestDto.getMaritalStatus())
                        .isInsuranceEnabled(statement.getAppliedOffer().getIsInsuranceEnabled())
                        .passportSeries(client.getPassport().getSeries())
                        .passportNumber(client.getPassport().getNumber())
                        .build());

        ScoringDataDto scoringDataDto = clientService.prepareScoringDataDto(statement, finishRegistrationRequestDto);

        assertNotNull(scoringDataDto);
        assertEquals(finishRegistrationRequestDto.getGender().toString(), scoringDataDto.getGender());
        assertEquals(statement.getAppliedOffer().getRequestedAmount(), scoringDataDto.getAmount());
        assertEquals(statement.getAppliedOffer().getTerm(), scoringDataDto.getTerm());
        assertEquals(loanStatementRequestDto.getBirthdate(), scoringDataDto.getBirthdate());
        assertEquals(loanStatementRequestDto.getFirstName(), scoringDataDto.getFirstName());
        assertEquals(loanStatementRequestDto.getLastName(), scoringDataDto.getLastName());
        assertEquals(finishRegistrationRequestDto.getEmployment(), scoringDataDto.getEmployment());
        assertEquals(finishRegistrationRequestDto.getAccountNumber(), scoringDataDto.getAccountNumber());
        assertEquals(loanStatementRequestDto.getPassportSeries(), scoringDataDto.getPassportSeries());
        assertEquals(statement.getAppliedOffer().getIsInsuranceEnabled(), scoringDataDto.getIsInsuranceEnabled());

        verify(clientMapper).update(client, finishRegistrationRequestDto);
        verify(clientRepository).save(client);
        verify(clientMapper).from(client, client.getPassport(), statement.getAppliedOffer());

    }

    private void assertClientEquals(Client expected, Client actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getBirthdate(), actual.getBirthdate());
        assertEquals(expected.getPassport(), actual.getPassport());
    }
}