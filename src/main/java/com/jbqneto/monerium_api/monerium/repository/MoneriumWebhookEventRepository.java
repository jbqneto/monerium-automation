package com.jbqneto.monerium_api.monerium.repository;

import com.jbqneto.monerium_api.monerium.entity.MoneriumWebhookEvent;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneriumWebhookEventRepository extends JpaRepository<MoneriumWebhookEvent, UUID> {
}
