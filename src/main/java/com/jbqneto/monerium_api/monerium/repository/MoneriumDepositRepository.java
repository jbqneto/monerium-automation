package com.jbqneto.monerium_api.monerium.repository;

import com.jbqneto.monerium_api.monerium.entity.MoneriumDeposit;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneriumDepositRepository extends JpaRepository<MoneriumDeposit, UUID> {
}
