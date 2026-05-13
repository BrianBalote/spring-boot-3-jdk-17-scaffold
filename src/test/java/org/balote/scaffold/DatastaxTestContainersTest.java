package org.balote.scaffold;

import com.datastax.oss.driver.api.core.CqlSession;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.cassandra.CassandraContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@Testcontainers
@DataCassandraTest
public class DatastaxTestContainersTest {

    static CassandraContainer cassandraContainer;

    @BeforeAll
    static void startCassandra() {
        cassandraContainer = new CassandraContainer("cassandra:4.1")
                .withExposedPorts(9042)
                .withEnv("CASSANDRA_AUTHENTICATOR", "PasswordAuthenticator")
                .withEnv("CASSANDRA_USER", "test_container_user")
                .withEnv("CASSANDRA_PASSWORD", "test_container_password_1234_")
                .withEnv("CASSANDRA_DC", "test_container_datacenter")
                .withStartupTimeout(Duration.ofMinutes(5))
                .waitingFor(
                        Wait.forListeningPort()
                                .withStartupTimeout(Duration.ofMinutes(5))
                );
        cassandraContainer
                .withInitScript("init.cql")
                .start();
    }

    @DynamicPropertySource
    static void registerCassandraProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cassandra.contact-points",
                () -> cassandraContainer.getHost() + ":" + cassandraContainer.getMappedPort(9042));
        registry.add("spring.cassandra.local-datacenter", cassandraContainer::getLocalDatacenter);
        registry.add("spring.cassandra.username", () -> "test_container_user");
        registry.add("spring.cassandra.password", () -> "test_container_password_1234_");
        registry.add("spring.cassandra.keyspace-name", () -> "test_container_keyspace");
    }

    @AfterAll
    static void stopCassandra() {
        if (cassandraContainer != null) cassandraContainer.stop();
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
}
