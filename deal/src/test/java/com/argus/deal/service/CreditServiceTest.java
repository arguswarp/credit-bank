package com.argus.deal.service;

import com.argus.deal.dto.CreditDto;
import com.argus.deal.entity.Credit;
import com.argus.deal.model.enums.CreditStatus;
import com.argus.deal.model.mapper.CreditMapper;
import com.argus.deal.repository.CreditRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * ${NAME}.
 *
 * @author Maxim Chistyakov
 */
@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @InjectMocks
    private CreditService creditService;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private CreditMapper creditMapper;

    @Test
    void save() {
        CreditDto creditDto = CreditDto.builder()
                .amount(BigDecimal.valueOf(100_000))
                .psk(BigDecimal.valueOf(120_000))
                .rate(BigDecimal.valueOf(10))
                .term(36)
                .build();

        Credit credit = Credit.builder()
                .amount(creditDto.getAmount())
                .psk(creditDto.getPsk())
                .rate(creditDto.getRate())
                .term(creditDto.getTerm())
                .build();

        when(creditMapper.creditDtoToCredit(creditDto)).thenReturn(credit);
        when(creditRepository.save(any())).thenReturn(credit);

        Credit saved = creditService.save(creditDto);
        assertNotNull(saved);
        assertEquals(creditDto.getAmount(), saved.getAmount());
        assertEquals(creditDto.getPsk(), saved.getPsk());
        assertEquals(creditDto.getRate(), saved.getRate());
        assertEquals(creditDto.getTerm(), saved.getTerm());
        assertEquals(CreditStatus.CALCULATED, saved.getCreditStatus());

        verify(creditMapper).creditDtoToCredit(creditDto);
        verify(creditRepository).save(credit);
    }
}