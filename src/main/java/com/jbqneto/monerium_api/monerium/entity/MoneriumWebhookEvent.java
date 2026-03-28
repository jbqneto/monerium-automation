package com.jbqneto.monerium_api.monerium.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "monerium_webhook_event")
public class MoneriumWebhookEvent {

    @Id
    private UUID id;

    @Column(name = "event_type", length = 100, nullable = false)
    private String eventType;

    @Column(name = "signature", length = 255)
    private String signature;

    @Column(name = "processed", nullable = false)
    private boolean processed;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;
}
