package org.balote.scaffold;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.cassandra.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.UUID;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
public class DatastaxTestContainersTest {

    @Container
    static CassandraContainer cassandraContainer = new CassandraContainer("cassandra:4.1")
            .withExposedPorts(9042);

    @Test
    void testDatastaxCassandra() {
        // Connect using Datastax CqlSession
        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(
                        cassandraContainer.getHost(),
                        cassandraContainer.getMappedPort(9042)))
                .withLocalDatacenter("datacenter1") // Cassandra default
                .build()) {

            // Create keyspace
            session.execute("CREATE KEYSPACE IF NOT EXISTS test_keyspace " +
                    "WITH replication = {'class':'SimpleStrategy','replication_factor':1};");

            // Use keyspace
            session.execute("USE test_keyspace;");

            // Create table
            session.execute("CREATE TABLE IF NOT EXISTS person (" +
                    "id UUID PRIMARY KEY, " +
                    "name text);");

            // Insert a row
            session.execute(SimpleStatement.builder(
                            "INSERT INTO person(id, name) VALUES (?, ?)")
                    .addPositionalValue(UUID.randomUUID())
                    .addPositionalValue("Alice")
                    .build());

            // Query the row count
            ResultSet rs = session.execute("SELECT COUNT(*) FROM person;");
            long count = Objects.requireNonNull(rs.one()).getLong(0);

            Assertions.assertThat(count).isEqualTo(1);
        }
    }
}
