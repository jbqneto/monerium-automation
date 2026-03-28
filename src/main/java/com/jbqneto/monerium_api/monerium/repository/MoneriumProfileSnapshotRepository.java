package com.jbqneto.monerium_api.monerium.repository;

import com.jbqneto.monerium_api.monerium.entity.MoneriumProfileSnapshot;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneriumProfileSnapshotRepository extends JpaRepository<MoneriumProfileSnapshot, UUID> {
}
