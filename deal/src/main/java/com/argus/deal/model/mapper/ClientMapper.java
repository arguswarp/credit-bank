package com.argus.deal.model.mapper;

import com.argus.deal.dto.LoanStatementRequestDto;
import com.argus.deal.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClientMapper {

    Client loanStatementRequestDtoToClient(LoanStatementRequestDto loanStatementRequestDto);

}
