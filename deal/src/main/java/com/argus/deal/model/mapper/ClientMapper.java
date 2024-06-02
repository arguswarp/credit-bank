package com.argus.deal.model.mapper;

import com.argus.deal.dto.FinishRegistrationRequestDto;
import com.argus.deal.dto.LoanOfferDto;
import com.argus.deal.dto.LoanStatementRequestDto;
import com.argus.deal.dto.ScoringDataDto;
import com.argus.deal.entity.Client;
import com.argus.deal.entity.Passport;
import org.mapstruct.*;

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
                .series(loanStatementRequestDto.getPassportSeries())
                .number(loanStatementRequestDto.getPassportNumber())
                .build();
        builder.passport(passport);
    }

    void update(@MappingTarget Client client, FinishRegistrationRequestDto finishRegistrationRequestDto);

    @Mapping(target = "issueDate", source = "passportIssueDate")
    @Mapping(target = "issueBranch", source = "passportIssueBranch")
    void updatePassport(@MappingTarget Passport passport, FinishRegistrationRequestDto finishRegistrationRequestDto);

    @Mapping(target = "amount", source = "loanOfferDto.requestedAmount")
    @Mapping(target = "passportSeries", source = "passport.series")
    @Mapping(target = "passportNumber", source = "passport.number")
    @Mapping(target = "passportIssueDate", source = "passport.issueDate")
    @Mapping(target = "passportIssueBranch", source = "passport.issueBranch")
    ScoringDataDto from(Client client, Passport passport, LoanOfferDto loanOfferDto);
}
