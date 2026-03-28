package com.jbqneto.monerium_api.monerium.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "monerium_deposit")
public class MoneriumDeposit {

    @Id
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "sender_iban", length = 64)
    private String senderIban;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "currency", length = 10, nullable = false)
    private String currency;

    @Column(name = "amount", precision = 19, scale = 8, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private MoneriumDepositStatus status;

    @Column(name = "processed_at")
    private Instant processedAt;
}
