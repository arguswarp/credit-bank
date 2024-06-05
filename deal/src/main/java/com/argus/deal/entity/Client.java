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

@Entity
@Table(name = "client", schema = "bank")
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Statement> statements;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Passport passport;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Employment employment;
}
