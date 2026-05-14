package org.balote.scaffold;

import com.datastax.oss.driver.api.core.CqlSession;
import lombok.extern.slf4j.Slf4j;
import org.balote.scaffold.shared.AbstractDatastaxCassandraBaseContainerTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

@ActiveProfiles("test-container")
@Slf4j
@DataCassandraTest
@Order(Integer.MAX_VALUE) // set as the last test class to run and call @AfterAll to stop the container
public class DatastaxCassandraLastContainerTest extends AbstractDatastaxCassandraBaseContainerTest {

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
    static void cleanUp() {
        stopCassandra();
    }
}
