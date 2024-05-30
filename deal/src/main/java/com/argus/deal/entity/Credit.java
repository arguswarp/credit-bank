package com.argus.deal.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Credit {

    @Id
    @Column(name = "credit_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @OneToOne(optional=false, mappedBy="credit")
    private Statement statement;

}
