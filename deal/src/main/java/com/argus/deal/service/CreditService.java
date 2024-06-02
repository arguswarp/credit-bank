package com.argus.deal.service;

import com.argus.deal.dto.CreditDto;
import com.argus.deal.entity.Credit;
import com.argus.deal.model.enums.CreditStatus;
import com.argus.deal.model.mapper.CreditMapper;
import com.argus.deal.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditService {

    private final CreditRepository creditRepository;

    private final CreditMapper creditMapper;

    public Credit save(CreditDto creditDto) {
        log.info("Saving credit {}", creditDto);
        Credit credit = creditMapper.creditDtoToCredit(creditDto);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        return creditRepository.save(credit);
    }
}
