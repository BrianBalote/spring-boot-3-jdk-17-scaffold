package org.balote.scaffold.shared;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.testcontainers.containers.Network;

@Slf4j
public abstract class SharedNetwork {

    private static Network network;

    private SharedNetwork() {
    }

    public static synchronized Network getInstance() {
        if (network == null) {
            network = Network.builder()
                    .createNetworkCmdModifier(cmd -> cmd.withName("db_network"))
                    .build();
        }
        return network;
    }

    @AfterAll
    public static synchronized void close() {
        log.info("[SharedNetwork][close()]");
        if (network != null) {
            network.close();
            network = null;
        }
    }
}