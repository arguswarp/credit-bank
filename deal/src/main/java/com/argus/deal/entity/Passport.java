package com.argus.deal.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Passport {

    @EqualsAndHashCode.Exclude
    private UUID id;

    private String series;

    private String number;

    private String issueBranch;

    private LocalDate issueDate;

}
