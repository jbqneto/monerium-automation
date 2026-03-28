package com.jbqneto.monerium_api.monerium.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "monerium_order")
public class MoneriumOrder {

    @Id
    private UUID id;

    @Column(name = "profile_id")
    private UUID profileId;

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "order_type", length = 50)
    private String orderType;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "chain", length = 50)
    private String chain;

    @Column(name = "amount", precision = 19, scale = 8)
    private BigDecimal amount;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
