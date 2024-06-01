package com.argus.deal.entity;

import com.argus.deal.model.enums.Gender;
import com.argus.deal.model.enums.MaritalStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "client", schema = "bank")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Client {

    @Id()
    @Column(name = "client_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String lastName;

    private String firstName;

    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthdate;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    private String accountNumber;

    @OneToOne(optional=false, mappedBy="client")
    @ToString.Exclude
    private Statement statement;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id", unique = true)
    @ToString.Exclude
    private Passport passport;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employment_id", unique = true)
    @ToString.Exclude
    private Employment employment;
}
