package org.balote.scaffold;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

@Slf4j
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
public class SybaseTestContainersTest {

    @Container
    static GenericContainer<?> sybaseContainer = new GenericContainer<>("datagrip/sybase:16.0")
            .withExposedPorts(5000)
            .withEnv("SAPASS", "myPassword")
            .withStartupTimeout(Duration.ofMinutes(5))
            .waitingFor(
                    Wait.forListeningPort()
                            .withStartupTimeout(Duration.ofMinutes(5))
            );

    @DynamicPropertySource
    static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () ->
                String.format("jdbc:sybase:Tds:%s:%d/testdb",
                        sybaseContainer.getHost(),
                        sybaseContainer.getMappedPort(5000)));
        registry.add("spring.datasource.username", () -> "sa");
        registry.add("spring.datasource.password", () -> "myPassword");
        registry.add("spring.datasource.driver-class-name", () -> "com.sybase.jdbc4.jdbc.SybDriver");
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testSybaseConnection() {
        // Create table
        jdbcTemplate.execute("CREATE TABLE person (id INT PRIMARY KEY, name VARCHAR(50))");

        // Insert row
        jdbcTemplate.update("INSERT INTO person (id, name) VALUES (?, ?)", 1, "Alice");

        // Query row count
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM person", Integer.class);
        Assertions.assertThat(count).isEqualTo(1);
    }
}
