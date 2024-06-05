package com.argus.deal.model.mapper;

import com.argus.deal.dto.FinishRegistrationRequestDto;
import com.argus.deal.dto.LoanOfferDto;
import com.argus.deal.dto.LoanStatementRequestDto;
import com.argus.deal.dto.ScoringDataDto;
import com.argus.deal.entity.Client;
import com.argus.deal.entity.Passport;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClientMapper {

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "middleName", source = "middleName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "birthdate", source = "birthdate")
    Client loanStatementRequestDtoToClient(LoanStatementRequestDto loanStatementRequestDto);

    @AfterMapping
    default void mapPassport(@MappingTarget Client.ClientBuilder builder, LoanStatementRequestDto loanStatementRequestDto) {
        Passport passport = Passport.builder()
                .id(UUID.randomUUID())
                .series(loanStatementRequestDto.getPassportSeries())
                .number(loanStatementRequestDto.getPassportNumber())
                .build();
        builder.passport(passport);
    }
    @Mapping(target = "employment.id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "passport.issueDate", source = "passportIssueDate")
    @Mapping(target = "passport.issueBranch", source = "passportIssueBranch")
    void update(@MappingTarget Client client, FinishRegistrationRequestDto finishRegistrationRequestDto);

    @Mapping(target = "amount", source = "loanOfferDto.requestedAmount")
    @Mapping(target = "passportSeries", source = "passport.series")
    @Mapping(target = "passportNumber", source = "passport.number")
    @Mapping(target = "passportIssueDate", source = "passport.issueDate")
    @Mapping(target = "passportIssueBranch", source = "passport.issueBranch")
    ScoringDataDto from(Client client, Passport passport, LoanOfferDto loanOfferDto);
}
