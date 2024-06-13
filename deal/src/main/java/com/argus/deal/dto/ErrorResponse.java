package com.argus.deal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ErrorResponse.
 *
 * @author Maxim Chistyakov
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private List<String> errors;

}
