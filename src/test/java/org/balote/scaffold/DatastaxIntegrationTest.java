package org.balote.scaffold;

import com.datastax.oss.driver.api.core.CqlSession;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

@Slf4j
@ActiveProfiles("dev")
@DataCassandraTest
class DatastaxIntegrationTest {

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
