package org.balote.scaffold.container.shared;

import lombok.Getter;
import org.testcontainers.cassandra.CassandraContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;
import java.util.Objects;

public class CassandraContainerSingleton {

    @Getter
    private static CassandraContainer cassandraContainer;

    public static void startCassandra() {
        cassandraContainer = new CassandraContainer("cassandra:4.1")
                .withExposedPorts(9042)
                .withEnv("CASSANDRA_AUTHENTICATOR", "PasswordAuthenticator")
                .withEnv("CASSANDRA_USER", "test_container_user")
                .withEnv("CASSANDRA_PASSWORD", "test_container_password_1234_!")
                .withEnv("CASSANDRA_DC", "test_container_datacenter")
                .withStartupTimeout(Duration.ofMinutes(5))
                .waitingFor(
                        Wait.forListeningPort()
                                .withStartupTimeout(Duration.ofMinutes(5))
                );
        cassandraContainer
                .withInitScript("init.cql") // create and use a keyspace
                .start();
    }

    public static void stopCassandra() {
        if (Objects.nonNull(cassandraContainer) && cassandraContainer.isRunning()) {
            cassandraContainer.stop();
        }
    }

    private CassandraContainerSingleton() {
    }
}
