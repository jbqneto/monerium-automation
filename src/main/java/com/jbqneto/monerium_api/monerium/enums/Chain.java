package com.jbqneto.monerium_api.monerium.enums;

public enum Chain {
    ETHEREUM("ethereum"),
    ETHEREUM_SEPOLIA("etherem"),
    BASE("base"),
    BASE_SEPOLIA("basesepolia"),
    POLYGON("polygon"),
    POLYGON_AMOY("amoy"),
    ARBITRUM("arbitrum");

    String id;

    Chain(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
