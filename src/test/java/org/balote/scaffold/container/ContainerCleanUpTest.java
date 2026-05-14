package org.balote.scaffold.container;

import lombok.extern.slf4j.Slf4j;
import org.balote.scaffold.container.shared.CassandraContainerSingleton;
import org.balote.scaffold.container.shared.SybaseContainerSingleton;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-container")
@Slf4j
@DataCassandraTest
@Order(Integer.MAX_VALUE) // set as the last test class to run and call @AfterAll to stop the container
public class ContainerCleanUpTest {

    @Test
    void test() {
    }

    @AfterAll
    static void cleanUp() {
        CassandraContainerSingleton.stopCassandra();
        SybaseContainerSingleton.stopSybase();
    }
}
