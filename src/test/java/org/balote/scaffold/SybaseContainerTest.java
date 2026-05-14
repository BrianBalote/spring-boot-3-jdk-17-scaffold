package org.balote.scaffold;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

@Slf4j
@ActiveProfiles("test-container")
@Testcontainers
@DataJpaTest
class SybaseContainerTest {

    static GenericContainer<?> sybaseContainer;

    @BeforeAll
    static void startSybase() {
        sybaseContainer = new GenericContainer<>("datagrip/sybase:16.0")
                .withExposedPorts(5000)
                .withEnv("SAPASS", "myPassword")
                .withStartupTimeout(Duration.ofMinutes(5))
                .waitingFor(
                        Wait.forListeningPort()
                                .withStartupTimeout(Duration.ofMinutes(5))
                );
        sybaseContainer.start();
    }

    @DynamicPropertySource
    static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () ->
                String.format("jdbc:sybase:Tds:%s:%d/testdb",
                        sybaseContainer.getHost(),
                        sybaseContainer.getMappedPort(5000)));
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testSybaseConnection() {
        jdbcTemplate.execute("CREATE TABLE person (id INT PRIMARY KEY, name VARCHAR(50))");
        jdbcTemplate.update("INSERT INTO person (id, name) VALUES (?, ?)", 1, "Alice");
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM person", Integer.class);
        log.info("Number of person records: {}", count);
        Assertions.assertThat(count).isEqualTo(1);
    }

    @AfterAll
    static void stopSybase() {
        if (sybaseContainer != null) {
            sybaseContainer.stop();
        }
    }
}
