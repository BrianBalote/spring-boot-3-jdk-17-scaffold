package org.balote.scaffold;

import com.datastax.oss.driver.api.core.CqlSession;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.cassandra.CassandraContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.Objects;

@ActiveProfiles("test-container")
@Slf4j
@Testcontainers
@DataCassandraTest
public class DatastaxCassandraContainerTest {

    static CassandraContainer cassandraContainer;

    @BeforeAll
    static void startCassandra() {
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

    @Autowired
    CqlSession cqlSession;

    @Test
    void test() {
        String version = Objects.requireNonNull(cqlSession.execute("SELECT release_version FROM system.local")
                        .one())
                .getString("release_version");
        log.info("Cassandra version: {}", version);
        Assertions.assertTrue(version != null && !version.isEmpty());
    }

    @AfterAll
    static void stopCassandra() {
        if (cassandraContainer != null) {
            cassandraContainer.stop();
        }
    }
}
