package com.argus.deal.entity;

import com.argus.deal.model.enums.Gender;
import com.argus.deal.model.enums.MaritalStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "client")
public class Client {

    @Id()
    @Column(name = "client_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String lastName;

    private String firstName;

    private String middleName;

    private LocalDate birthdate;

    private String email;

    private Gender gender;

    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    private String accountNumber;

    @OneToOne(optional=false, mappedBy="client")
    private Statement statement;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id", unique = true)
    private Passport passport;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employment_id", unique = true)
    private Employment employment;
}
