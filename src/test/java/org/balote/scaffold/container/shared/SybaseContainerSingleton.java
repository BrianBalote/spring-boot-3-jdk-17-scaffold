package org.balote.scaffold.container.shared;

import lombok.Getter;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;
import java.util.Objects;

public class SybaseContainerSingleton {

    @Getter
    static GenericContainer<?> sybaseContainer;

    public static void startSybase() {
        sybaseContainer = new GenericContainer<>("datagrip/sybase:16.0")
                .withExposedPorts(5000)
                .withEnv("SAPASS", "myPassword")
                .withStartupTimeout(Duration.ofMinutes(5))
                .waitingFor(
                        Wait.forListeningPort()
                                .withStartupTimeout(Duration.ofMinutes(5))
                );
        sybaseContainer.start();
    }

    public static void stopSybase() {
        if(Objects.nonNull(sybaseContainer) && sybaseContainer.isRunning()) {
            sybaseContainer.stop();
        }
    }

    private SybaseContainerSingleton() {}
}
