package com.argus.deal.model.mapper;

import com.argus.deal.dto.CreditDto;
import com.argus.deal.entity.Credit;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CreditMapper {

    Credit creditDtoToCredit(CreditDto creditDto);
}
