package com.argus.calculator.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ErrorResponse {

    private List<String> errors;
}
