package org.balote.scaffold.integration;

import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@Disabled
@ActiveProfiles("dev")
@SpringBootTest
public class SybaseIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
}
