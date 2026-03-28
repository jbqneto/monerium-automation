package com.jbqneto.monerium_api.monerium.service;

import com.jbqneto.monerium_api.monerium.dto.response.MoneriumProfileResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumProfileSummaryResponse;
import java.util.UUID;

public interface MoneriumProfileSnapshotService {

    void saveSnapshot(
        UUID userId,
        MoneriumProfileSummaryResponse profileSummary,
        MoneriumProfileResponse profile
    );
}
