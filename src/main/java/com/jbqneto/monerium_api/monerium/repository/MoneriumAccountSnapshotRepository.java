package com.jbqneto.monerium_api.monerium.repository;

import com.jbqneto.monerium_api.monerium.entity.MoneriumAccountSnapshot;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneriumAccountSnapshotRepository extends JpaRepository<MoneriumAccountSnapshot, UUID> {

    void deleteByProfileId(UUID profileId);
}
