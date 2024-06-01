package com.argus.deal.entity;

import com.argus.deal.dto.LoanOfferDto;
import com.argus.deal.dto.StatementStatusHistoryDto;
import com.argus.deal.model.enums.Status;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "statement")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Statement {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", unique = true)
    private Client client;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creationDate;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private LoanOfferDto appliedOffer;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime signDate;

    private String sesCode;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private StatementStatusHistoryDto statusHistory;

}
