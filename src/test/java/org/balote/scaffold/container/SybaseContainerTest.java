package org.balote.scaffold.container;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.balote.scaffold.container.extendable.AbstractSybaseBaseContainerTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@ActiveProfiles("test-container")
@DataJpaTest
class SybaseContainerTest extends AbstractSybaseBaseContainerTest {

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
}
