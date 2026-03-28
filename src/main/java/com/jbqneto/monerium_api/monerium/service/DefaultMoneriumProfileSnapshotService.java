package com.jbqneto.monerium_api.monerium.service;

import com.jbqneto.monerium_api.monerium.dto.response.MoneriumProfileResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumProfileSummaryResponse;
import com.jbqneto.monerium_api.monerium.entity.MoneriumAccountSnapshot;
import com.jbqneto.monerium_api.monerium.entity.MoneriumProfileSnapshot;
import com.jbqneto.monerium_api.monerium.mapper.MoneriumProfileSnapshotMapper;
import com.jbqneto.monerium_api.monerium.repository.MoneriumAccountSnapshotRepository;
import com.jbqneto.monerium_api.monerium.repository.MoneriumProfileSnapshotRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultMoneriumProfileSnapshotService implements MoneriumProfileSnapshotService {

    private final MoneriumProfileSnapshotMapper moneriumProfileSnapshotMapper;
    private final MoneriumProfileSnapshotRepository moneriumProfileSnapshotRepository;
    private final MoneriumAccountSnapshotRepository moneriumAccountSnapshotRepository;

    public DefaultMoneriumProfileSnapshotService(
        MoneriumProfileSnapshotMapper moneriumProfileSnapshotMapper,
        MoneriumProfileSnapshotRepository moneriumProfileSnapshotRepository,
        MoneriumAccountSnapshotRepository moneriumAccountSnapshotRepository
    ) {
        this.moneriumProfileSnapshotMapper = moneriumProfileSnapshotMapper;
        this.moneriumProfileSnapshotRepository = moneriumProfileSnapshotRepository;
        this.moneriumAccountSnapshotRepository = moneriumAccountSnapshotRepository;
    }

    @Override
    @Transactional
    public void saveSnapshot(
        UUID userId,
        MoneriumProfileSummaryResponse profileSummary,
        MoneriumProfileResponse profile
    ) {
        Instant syncedAt = Instant.now();
        MoneriumProfileSnapshot profileSnapshot = moneriumProfileSnapshotMapper.toProfileSnapshot(
            userId,
            profileSummary,
            profile,
            syncedAt
        );
        List<MoneriumAccountSnapshot> accountSnapshots = moneriumProfileSnapshotMapper.toAccountSnapshots(
            profile.id(),
            profile.accounts(),
            syncedAt
        );

        moneriumProfileSnapshotRepository.save(profileSnapshot);
        moneriumAccountSnapshotRepository.deleteByProfileId(profile.id());
        moneriumAccountSnapshotRepository.saveAll(accountSnapshots);
    }
}
