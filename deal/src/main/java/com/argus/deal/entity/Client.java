package com.argus.deal.entity;

import com.argus.deal.model.enums.Gender;
import com.argus.deal.model.enums.MaritalStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Client.
 *
 * @author Maxim Chistyakov
 */
@Entity
@Table(name = "client")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthdate;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "marital_status")
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "passport", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Passport passport;

    @Column(name = "employment", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Employment employment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Statement> statements;
}
