package com.jbqneto.monerium_api.monerium.mapper;

import com.jbqneto.monerium_api.monerium.dto.response.MoneriumAccountResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumProfileResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumProfileSummaryResponse;
import com.jbqneto.monerium_api.monerium.entity.MoneriumAccountSnapshot;
import com.jbqneto.monerium_api.monerium.entity.MoneriumProfileSnapshot;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class MoneriumProfileSnapshotMapper {

    public MoneriumProfileSnapshot toProfileSnapshot(
        UUID userId,
        MoneriumProfileSummaryResponse profileSummary,
        MoneriumProfileResponse profile,
        Instant syncedAt
    ) {
        MoneriumProfileSnapshot snapshot = new MoneriumProfileSnapshot();
        snapshot.setId(profile.id());
        snapshot.setUserId(userId);
        snapshot.setKind(profileSummary != null ? profileSummary.kind() : null);
        snapshot.setName(profile.name());
        snapshot.setKycState(profile.kyc() != null ? profile.kyc().state() : null);
        snapshot.setKycOutcome(profile.kyc() != null ? profile.kyc().outcome() : null);
        snapshot.setSyncedAt(syncedAt);
        return snapshot;
    }

    /**
     * Rebuilds the account snapshot list from the latest Monerium response so
     * persistence stays a faithful copy of the remote profile state.
     */
    public List<MoneriumAccountSnapshot> toAccountSnapshots(
        UUID profileId,
        List<MoneriumAccountResponse> accounts,
        Instant syncedAt
    ) {
        return accounts.stream()
            .map(account -> toAccountSnapshot(profileId, account, syncedAt))
            .toList();
    }

    private MoneriumAccountSnapshot toAccountSnapshot(
        UUID profileId,
        MoneriumAccountResponse account,
        Instant syncedAt
    ) {
        MoneriumAccountSnapshot snapshot = new MoneriumAccountSnapshot();
        snapshot.setId(account.id());
        snapshot.setProfileId(profileId);
        snapshot.setAddress(account.address());
        snapshot.setCurrency(account.currency());
        snapshot.setChain(account.chain());
        snapshot.setIban(account.iban());
        snapshot.setStandard(account.standard());
        snapshot.setSyncedAt(syncedAt);
        return snapshot;
    }
}
