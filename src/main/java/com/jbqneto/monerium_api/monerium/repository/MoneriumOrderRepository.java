package com.jbqneto.monerium_api.monerium.repository;

import com.jbqneto.monerium_api.monerium.entity.MoneriumOrder;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneriumOrderRepository extends JpaRepository<MoneriumOrder, UUID> {
}
