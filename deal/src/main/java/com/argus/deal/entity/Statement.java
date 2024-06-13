package com.argus.deal.entity;

import com.argus.deal.dto.LoanOfferDto;
import com.argus.deal.dto.StatementStatusHistoryDto;
import com.argus.deal.model.enums.Status;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Statement.
 *
 * @author Maxim Chistyakov
 */
@Entity
@Table(name = "statement")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Statement {

    @Id
    @Column(name = "statement_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    @ToString.Exclude
    private Client client;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id", unique = true)
    @ToString.Exclude
    private Credit credit;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "applied_offer", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private LoanOfferDto appliedOffer;

    @Column(name = "sign_date")
    private LocalDateTime signDate;

    @Column(name = "ses_code")
    private String sesCode;

    @Column(name = "status_history", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private List<StatementStatusHistoryDto> statusHistory;

}
