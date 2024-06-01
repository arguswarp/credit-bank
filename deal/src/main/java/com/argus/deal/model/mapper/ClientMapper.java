package com.argus.deal.model.mapper;

import com.argus.deal.dto.LoanStatementRequestDto;
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
}
