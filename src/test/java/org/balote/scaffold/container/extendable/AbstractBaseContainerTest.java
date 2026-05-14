package org.balote.scaffold.container.extendable;

import org.testcontainers.DockerClientFactory;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractBaseContainerTest {

    static {
        // Fail fast if Docker is not available
        if (!DockerClientFactory.instance().isDockerAvailable()) {
            throw new IllegalStateException("Docker is not available! Cannot start test containers.");
        }
    }
}
