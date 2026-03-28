package com.jbqneto.monerium_api.config;

import com.jbqneto.monerium_api.shared.enums.Asset;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.dca")
public record DcaProperties(
    BigDecimal singleTrancheLimit,
    BigDecimal doubleTrancheLimit,
    BigDecimal minimumAssetExecutionAmount,
    Map<Asset, Integer> allocations
) {}
