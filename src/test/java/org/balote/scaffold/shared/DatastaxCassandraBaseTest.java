package org.balote.scaffold.shared;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.cassandra.CassandraContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Testcontainers
@DataCassandraTest
public abstract class DatastaxCassandraBaseTest {

    static Instant startTime;
    static CassandraContainer cassandraContainer;

    @BeforeAll
    static void checkCassandra() {
        if (cassandraContainer == null) {
            startCassandra();
            return;
        }
        if (!cassandraContainer.isRunning()) {
            startCassandra();
        }
    }

    protected static void startCassandra() {
        startTime = Instant.now();
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

    @DynamicPropertySource
    static void registerCassandraProperties(DynamicPropertyRegistry registry) {
        // overwrite spring.cassandra.contact-points because the port used by the container can change
        registry.add("spring.cassandra.contact-points",
                () -> cassandraContainer.getHost() + ":" + cassandraContainer.getMappedPort(9042));
    }

    protected static void stopCassandra() {
        if (cassandraContainer != null && cassandraContainer.isRunning()) {
            cassandraContainer.stop();
            log.info("Full test duration: {}", Duration.between(startTime, Instant.now()));
        }
    }
}
