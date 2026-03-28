package com.jbqneto.monerium_api.monerium.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "monerium_profile_snapshot")
public class MoneriumProfileSnapshot {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "kind", length = 50)
    private String kind;

    @Column(name = "name")
    private String name;

    @Column(name = "kyc_state", length = 50)
    private String kycState;

    @Column(name = "kyc_outcome", length = 50)
    private String kycOutcome;

    @Column(name = "synced_at", nullable = false)
    private Instant syncedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKycState() {
        return kycState;
    }

    public void setKycState(String kycState) {
        this.kycState = kycState;
    }

    public String getKycOutcome() {
        return kycOutcome;
    }

    public void setKycOutcome(String kycOutcome) {
        this.kycOutcome = kycOutcome;
    }

    public Instant getSyncedAt() {
        return syncedAt;
    }

    public void setSyncedAt(Instant syncedAt) {
        this.syncedAt = syncedAt;
    }
}
