package com.argus.deal.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "passport")
public class Passport {

    @Id
    @Column(name = "passport_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String series;

    private String number;

    private String issueBranch;

    private LocalDate issueDate;

    @OneToOne(optional=false, mappedBy="passport")
    private Client client;
}
