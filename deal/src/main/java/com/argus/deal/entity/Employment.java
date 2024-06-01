package com.argus.deal.entity;

import com.argus.deal.model.enums.EmploymentStatus;
import com.argus.deal.model.enums.Position;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "employment", schema = "bank")
@Data
public class Employment {

    @Id
    @Column(name = "employment_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    private String employerINN;

    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    private Position position;

    private Integer workExperienceTotal;

    private Integer workExperienceCurrent;

    @OneToOne(optional = false, mappedBy = "employment")
    @ToString.Exclude
    private Client client;
}
