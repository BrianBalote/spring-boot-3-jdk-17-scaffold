package org.balote.scaffold.container.extendable;

import lombok.extern.slf4j.Slf4j;
import org.balote.scaffold.container.shared.CassandraContainerSingleton;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@Slf4j
public abstract class AbstractCassandraBaseContainerTest extends AbstractBaseContainerTest {

    @BeforeAll
    static void startCassandra() {
        CassandraContainerSingleton.startCassandra();
    }

    @DynamicPropertySource
    static void registerCassandraProperties(DynamicPropertyRegistry registry) {
        // overwrite spring.cassandra.contact-points because the port used by the container can change
        registry.add("spring.cassandra.contact-points",
                () -> CassandraContainerSingleton.getCassandraContainer().getHost() + ":"
                        + CassandraContainerSingleton.getCassandraContainer().getMappedPort(9042));
    }
}
