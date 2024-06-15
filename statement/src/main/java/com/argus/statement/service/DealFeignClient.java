package com.argus.statement.service;

import com.argus.statement.dto.LoanOfferDto;
import com.argus.statement.dto.LoanStatementRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * DealFeignClient
 *
 * @author Maxim Chistyakov
 */
@FeignClient(name = "deal-client", url = "${spring.cloud.openfeign.client.config.deal-client.url}")
public interface DealFeignClient {

    @PostMapping("statement")
    List<LoanOfferDto> getLoanOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto);

    @PostMapping("offer/select")
    void selectOffer(@RequestBody LoanOfferDto loanOfferDto);
}
